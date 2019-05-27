package org.tangshihao.study.springboot.entity;

public class DataDimension {
    private String dimensionSource;
    private String dimensionName;
    private Integer departId;
    private String dimensionId;

    public String getDimensionSource() {
        return dimensionSource;
    }

    public void setDimensionSource(String dimensionSource) {
        this.dimensionSource = dimensionSource;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }
}
