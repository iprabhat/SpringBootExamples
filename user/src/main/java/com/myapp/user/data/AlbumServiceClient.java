package com.myapp.user.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.myapp.user.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

//@FeignClient(name = "album", fallback = AlbumsFallback.class)
@FeignClient(name = "album", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumServiceClient>{

	@Override
	public AlbumServiceClient create(Throwable cause) {
		return new AlbumServiceClientFallback(cause);
	}
}

class AlbumServiceClientFallback implements AlbumServiceClient{

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private Throwable cause;
	public AlbumServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}
	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		if(cause instanceof FeignException && ((FeignException) cause).status() == 404) {
			LOGGER.error(String.format("404 error occured when getAlbums was called with user Id: %s, Error message: %s", 
					id, cause.getLocalizedMessage()));
		}
		else {
			LOGGER.error("Some other error occurred: " + cause.getLocalizedMessage());
		}
		return new ArrayList<>();
	}
}

/*
@Component
class AlbumsFallback implements AlbumServiceClient{

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		return new ArrayList<>();
	}
}
*/