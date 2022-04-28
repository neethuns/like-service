package com.maveric.demo.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class HystrixFallBackFactory implements FallbackFactory<UserFeign> {
    @Override
    public UserFeign create(Throwable cause) {
        System.out.println("fallback; reason was: " + cause.getMessage());
        return null;
    }

}
