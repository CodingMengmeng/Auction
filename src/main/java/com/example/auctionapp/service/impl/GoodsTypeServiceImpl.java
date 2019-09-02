package com.example.auctionapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auctionapp.dao.AuctionGoodsMapper;
import com.example.auctionapp.dao.GoodsTypeMapper;
import com.example.auctionapp.dao.ImagesMapper;
import com.example.auctionapp.entity.AuctionGoods;
import com.example.auctionapp.entity.GoodsType;
import com.example.auctionapp.entity.Images;
import com.example.auctionapp.service.IGoodsTypeService;
import com.example.auctionapp.util.BeanUtil;
import com.example.auctionapp.vo.ClassifyVo;
import com.example.auctionapp.vo.HomeGoodsVo;
import com.example.auctionapp.vo.re.HomeGoodsRe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拍品类型 服务实现类
 * </p>
 *
 * @author 孔邹祥
 * @since 2019-05-13
 */
@Service
public class GoodsTypeServiceImpl implements IGoodsTypeService {

    @Resource
    GoodsTypeMapper goodsTypeMapper;

    @Resource
    AuctionGoodsMapper auctionGoodsMapper;

    @Resource
    ImagesMapper imagesMapper;

    @Override

    public List<Map<String, Object>> selectHierarchical() {
        List<Map<String, Object>> list = new ArrayList<>();


        List<GoodsType> parentLevel = goodsTypeMapper.selectList(new QueryWrapper<GoodsType>()
                .eq("rank", 1)
                .orderByAsc("sort"));
        parentLevel.forEach((x) -> {

            Map<String, Object> map = BeanUtil.objectToMap(x);
            List<Map<String, Object>> subsetList = goodsTypeMapper.selectMaps(new QueryWrapper<GoodsType>()
                    .eq("pt_id", x.getId())
                    .orderByAsc("sort"));
//                前端需要的字段
            subsetList.forEach((m) -> {
                m.put("value", m.get("id"));
                m.put("label", m.get("name"));
                m.put("typeImg", m.get("type_img"));
            });
//                前端需要的字段
            if (map != null) {
                map.put("value", x.getId());
                map.put("label", x.getName());
                map.put("children", subsetList);
            }
            list.add(map);
        });
        return list;
    }

    @Override
    public List<ClassifyVo> getOneClassify() {
        List<ClassifyVo> classifyVos = goodsTypeMapper.selectOneClassify();
        return classifyVos;
    }

    /**
     * 查询拍品，根据拍品名称和拍品分类
     *
     * @return Result
     * @author MaHC
     */
    @Override
    public IPage<HomeGoodsVo> getAuctionGoodsWhereType(HomeGoodsRe homeGoodsRe) {
        IPage iPage = new Page(homeGoodsRe.getPage(),homeGoodsRe.getRows());
        List<HomeGoodsVo> homeGoodsVos = goodsTypeMapper.selectAuctionGoodsWhereType(iPage, homeGoodsRe.getParentId(), homeGoodsRe.getName());
        iPage.setRecords(homeGoodsVos);
        return iPage;
    }
}
