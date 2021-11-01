package lxy.personal.website.dao;

import lxy.personal.website.entity.Catalogue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author lxy
 * @Date 2021/10/31 17:10
 * @Description
 */

@Mapper
public interface CatalogueMapper {
    List<Catalogue> getAllCata();
}
