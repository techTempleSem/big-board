package com.fastcaompus.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteIdRequest {
    @NonNull
    private String id;
    @NonNull
    private String password;
}
