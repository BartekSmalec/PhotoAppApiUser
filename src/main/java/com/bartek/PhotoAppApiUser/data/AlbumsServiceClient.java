package com.bartek.PhotoAppApiUser.data;

import com.bartek.PhotoAppApiUser.model.AlbumResponseModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;


@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {
    @GetMapping("/users/{id}/albums")
    @CircuitBreaker(name="albums-ws", fallbackMethod="getAlbumsFallback")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);

    default List<AlbumResponseModel> getAlbumsFallback(String id, Throwable exception){
        System.out.printf("Param = " + id);
        System.out.printf("Exception message: " + exception.getMessage());
        return new ArrayList<>();
    }
}


