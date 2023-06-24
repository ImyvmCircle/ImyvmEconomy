package com.imyvm.economy.api;

/**
 * 当你调用addMoney方法时，你需要提供TradeType来完成税收相关的设置。
 * 也可以用TradeTypeEnumRegistrar来新建自己的枚举类型。
 */
public class TradeTypeEnum{
    /**
     * 提供交易类型
     * 默认有PAY和DUTYFREE
     * 你也可以自己添加
     */
    public enum TradeType implements TradeTypeExtension {
        PAY,
        DUTYFREE;

        private Double tax;

        @Override
        public Double getTax() {
            return this.tax;
        }

        @Override
        public void setTax(Double tax) {
            this.tax = tax;
        }
    }

    public interface TradeTypeExtension {
        Double getTax();

        void setTax(Double tax);
    }


}

