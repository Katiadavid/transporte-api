package com.transporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(@NotBlank @Size(min = 3, max = 200) String endereco) {}
```

        ## `src/main/java/com/transporte/dto/MercadoriaDto.java`

        ```java
package com.transporte.dto;

import com.transporte.model.Mercadoria;

import jakarta.validation.constraints.*;

public final class MercadoriaDto {
    private MercadoriaDto() {}

    public record Request(
            @NotNull @Positive Double peso,
            @NotBlank @Size(min = 2, max = 60) String tipo,
            @NotNull Integer idP) {}

    public record Response(Integer id, Double peso, String tipo, Integer idP, String passageiro) {
        public static Response from(Mercadoria m) {
            return new Response(m.getId(), m.getPeso(), m.getTipo(),
                    m.getPassageiro().getId(), m.getPassageiro().getNome());
        }
    }
}