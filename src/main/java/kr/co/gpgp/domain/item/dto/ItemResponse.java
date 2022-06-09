package kr.co.gpgp.domain.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponse {

    @JsonIgnore
    private Long id;

    private int price;

    private int stockQuantity;

    private String name;

    private int weight;

    private String code;

    private String imageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate releaseDate;

    @Builder
    private ItemResponse(Long id, int price, int stockQuantity, String name, int weight, String code, String imageUrl, LocalDate releaseDate) {
        this.id = id;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
    }
}
