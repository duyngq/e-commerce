package com.store.exception.localization;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LocalizationException {
    private String errorCode;
    private String description;

    @JsonIgnore
    public LocalizationException cloneAndAppendDescription(String append){
        return new LocalizationException(errorCode, description + append);
    }
}
