package lxy.personal.website.controller;

import lombok.extern.slf4j.Slf4j;
import lxy.personal.website.entity.Catalogue;
import lxy.personal.website.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author lxy
 * @Date 2021/10/31 17:08
 * @Description
 */
@Slf4j
@RestController
public class CatalogueController {

    @Autowired
    private CatalogueService catalogueService;


    @PostMapping("/lxy/cata")
    public List<Catalogue> getAllCata(){
        List<Catalogue> allCata = catalogueService.getAllCata();
        return allCata;
    }

    @PostMapping("/lxy/m/updcata")
    public void updCata(){
        List<Catalogue> allCata = catalogueService.getAllCata();
    }

    @GetMapping("/lxy/grant/{grantcode}")
    public int grantSet(@PathVariable("grantcode") String grantcode){
        if(grantcode.equals("0307")){
            return 200;
        }else{
            return 403;
        }
    }
}
