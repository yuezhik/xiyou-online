package com.yuezhik.educms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuezhik.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuezhik.educms.entity.vo.BannerQuery;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author yuezhik
 * @since 2022-04-25
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> getAllBanner();

    void pageQuery(Page<CrmBanner> bannerPage, BannerQuery bannerQuery);

    List<CrmBanner> selectAllBanner();
}
