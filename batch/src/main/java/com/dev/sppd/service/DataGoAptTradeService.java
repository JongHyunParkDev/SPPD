package com.dev.sppd.service;

import com.dev.sppd.entity.AptRentEntity;
import com.dev.sppd.entity.AptTradeEntity;
import com.dev.sppd.repository.AptRentRepository;
import com.dev.sppd.repository.AptTradeRepository;
import com.dev.sppd.vo.AptDataVo;
import com.dev.sppd.vo.AptRentDataVo;
import com.dev.sppd.vo.AptTradeDataVo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.util.List;


// Data.go.kr 에서 받아올 데이터
@Service
@RequiredArgsConstructor
@Slf4j
public class DataGoAptTradeService {
    private final AptTradeRepository aptTradeRepository;

    private final AptRentRepository aptRentRepository;

    @PostConstruct
    public void init() {
        insertDataGoAptTradeInfo("11110", "201512");
        insertDataGoAptRentInfo("11110", "201512");

    }
    //region #매매
    public void insertDataGoAptTradeInfo(String cityCode, String yearMonth) {
        logger.info("insertDataGoAptTradeInfo");
        String urlStr = String.format("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s",
                serviceKey, cityCode, yearMonth); /*URL*/

        RestTemplate restTemplate = new RestTemplate();
        URI url = URI.create(urlStr);

        ResponseEntity<AptDataVo<AptTradeDataVo.TradeItem>> tradeResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AptDataVo<AptTradeDataVo.TradeItem>>() {}
        );

        if (tradeResponseEntity.getStatusCode() == HttpStatus.OK
                && tradeResponseEntity.getBody().getResponse().getHeader().getResultCode().equals("00")) {

            List<AptTradeDataVo.TradeItem> list = tradeResponseEntity.getBody().getResponse().getBody().getItems().getItem();
            logger.info(list.toString());

            list.forEach(item -> {
                aptTradeRepository.save(new AptTradeEntity(item));
            });
        }
    }

    //endregion

    //region #월세 전세
    public void insertDataGoAptRentInfo(String cityCode, String yearMonth) {
        logger.info("insertDataGoAptRentInfo");
        String urlStr = String.format("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptRent?serviceKey=%s&LAWD_CD=%s&DEAL_YMD=%s&numOfRows=9999",
                serviceKey, cityCode, yearMonth); /*URL*/

        RestTemplate restTemplate = new RestTemplate();
        URI url = URI.create(urlStr);
        ResponseEntity<AptDataVo<AptRentDataVo.RentItem>> rentResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AptDataVo<AptRentDataVo.RentItem>>() {}
        );
        if (rentResponseEntity.getStatusCode() == HttpStatus.OK
                && rentResponseEntity.getBody().getResponse().getHeader().getResultCode().equals("00")) {

            List<AptRentDataVo.RentItem> list = rentResponseEntity.getBody().getResponse().getBody().getItems().getItem();
            logger.info(list.toString());

            list.forEach(item -> {
                aptRentRepository.save(new AptRentEntity(item));
            });
        }
    }

    //endregion

    private final String DataGoAptTradeKey = "APT_TRADE";

    private final String DataGoAptRentKey = "APT_RENT";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${datago.serviceKey}")
    private String serviceKey;

}
