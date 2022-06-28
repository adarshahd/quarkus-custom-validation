package com.gigathinking.validations.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TodoCreateRequest {

    @NotBlank
    public String title;

    @NotNull
    @JsonProperty("is_completed")
    public Boolean isCompleted;
}
