package org.tangshihao.study.springboot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.tangshihao.study.springboot.entity.DataDimension;

@Mapper
public interface DataDimensionMapper {
    int insert(DataDimension dataDimension);
}
