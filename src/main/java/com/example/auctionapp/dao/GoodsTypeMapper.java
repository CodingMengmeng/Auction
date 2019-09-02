package com.example.auctionapp.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.auctionapp.entity.GoodsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auctionapp.vo.ClassifyVo;
import com.example.auctionapp.vo.HomeGoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品类型 Mapper 接口
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {


    /**
     * 分层查找出一级二级分类
     *
     * @return Map<GoodsType, GoodsType>
     * @author 孔邹祥
     */
    List<Map<String, Object>> selectHierarchical();

    /**
     * 分层查找出一级分类
     *
     * @return Result
     * @author MaHC
     */
    List<ClassifyVo> selectOneClassify();

    /**
     * 查询拍品，根据拍品名称和拍品分类
     *
     * @return Result
     * @author MaHC
     */
    List<HomeGoodsVo> selectAuctionGoodsWhereType(IPage iPage, @Param("parentId")Integer parentId,  @Param("name")String name);
}
