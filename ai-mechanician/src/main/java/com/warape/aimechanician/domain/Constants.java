package com.warape.aimechanician.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apeto
 * @create 2023/3/27 11:31
 */
public interface Constants {

  Integer CONTEXT_LIMIT = 5;
  Integer CONTEXT_SPLIT = 100;

  @Getter
  @AllArgsConstructor
  enum ResponseEnum implements StatusEnumSupport {
    LOGIN_ERROR(4001, "登录失败~"),
    NOT_LOGIN(4002, "请登录后再进行使用~"),
    BALANCE_INSUFFICIENT(4003, "您的余额不足请充值后再使用~"),
    MEMBER_EXP(4004, "您的会员已过期，请充值~"),
    MEMBER_LIMIT_COUNT(4005, "您的提问次数不足，请充值~"),
    PHONE_EXIST(4006, "该手机号已存在"),
    USER_EXIST(4008, "用户不存在,请先注册~"),
    QR_INVALID(4009, "二维码已失效"),
    WAITING_FOLLOW(4010, "正在登陆请稍后"),
    MEMBER_RIGHTS_EXIST(4011, "此会员卡权益已存在，请更换绑定~"),

    ;

    private Integer code;
    private String message;
  }


  @Getter
  @AllArgsConstructor
  enum PayStateEnum {
    // 支付状态 0: 支付中 1: 已支付 2: 支付失败 3: 支付取消

    PAYING(0, "支付中"),
    SUCCESS(1, "支付成功"),
    ERROR(2, "支付失败"),
    CANCEL(3, "支付取消"),
    REFUND(4, "已退款"),
    ;
    private Integer state;
    private String desc;

  }

  @Getter
  @AllArgsConstructor
  enum AliPayStateEnum {

    TRADE_CLOSED("TRADE_CLOSED", "交易关闭", PayStateEnum.CANCEL),
    TRADE_FINISHED("TRADE_FINISHED", "交易结束，不可退款", PayStateEnum.REFUND),
    TRADE_SUCCESS("TRADE_SUCCESS", "支付中", PayStateEnum.SUCCESS),
    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "交易创建", PayStateEnum.PAYING),
    ;
    private String state;
    private String desc;
    private PayStateEnum sysPayStateEnum;

    public static PayStateEnum getByPayStateEnum (String state) {
      for (AliPayStateEnum value : AliPayStateEnum.values()) {
        if (value.getState().equals(state)) {
          return value.getSysPayStateEnum();
        }
      }
      return AliPayStateEnum.TRADE_CLOSED.getSysPayStateEnum();
    }
  }


  @Getter
  @AllArgsConstructor
  enum PayTypeEnum {
//    支付类型 10:微信 20:支付宝

    WECHAT(10, "微信"),
    ALI_PAY(20, "支付宝"),
    ;
    private Integer payType;
    private String desc;
  }

  @Getter
  @AllArgsConstructor
  enum MemberPermissionTypeEnum {
    VIEW(1, "前端显示"),
    MANAGER(2, "后端显示"),
    ;

    private Integer type;
    private String desc;
  }

  @Getter
  @AllArgsConstructor
  enum DescriptionTypeEnum {
    RECHARGE(1, "充值"),
    RECOVERY(2, "失效回收"),
    REFUND(3, "退款"),
    CONSUMPTION(3, "消费"),
    ;

    private Integer type;
    private String desc;
  }

  @Getter
  @AllArgsConstructor
  enum DirectionTypeTypeEnum {
    IN(10, "收入"),
    OUT(20, "支出"),
    ;

    private Integer type;
    private String desc;
  }

  @Getter
  @AllArgsConstructor
  enum ExchangeCardStateEnum {
    EXCHANGE(1, "已兑换"),
    EXPIRES(2, "已过期"),
    ;

    private Integer state;
    private String desc;
  }

  @Getter
  @AllArgsConstructor
  enum MemberStateEnum {
    DOWN(0, "已下线"),
    ONLINE(1, "已上线"),
    ;

    private Integer state;
    private String desc;
  }

  @Getter
  @AllArgsConstructor
  enum LoginTypeEnum {
    EMAIL(1, "邮箱"),
    PHONE(2, "手机"),
    MP(3, "公众号"),
    ;

    private Integer type;
    private String desc;

    public static LoginTypeEnum getByEnum (Integer type) {
      for (LoginTypeEnum value : LoginTypeEnum.values()) {
        if (value.getType().equals(type)) {
          return value;
        }
      }
      return null;
    }
  }
}
