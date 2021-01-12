package ua.training.top.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.AbstractBaseEntity;

import java.net.URI;

class ResponseEntityUtil {
    public static <T extends AbstractBaseEntity> ResponseEntity<T> getResponseEntity(T object  , String url) {
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url + "/{id}")
                .buildAndExpand(object.getId())
                .toUri();
        ResponseEntity<T> responseEntity = ResponseEntity.created(uriOfNewResource).body(object);
        return responseEntity;
    }
}
