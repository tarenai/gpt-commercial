/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-14 13:22:03
 */
import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useRef,
} from "react";
import _ from "lodash";
import QRCode from "qrcode.react";
import aliPay from "../../../../../public/assets/imgs/ali-pay.png";
import wxPay from "../../../../../public/assets/imgs/wx-pay.png";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import { messageFn } from "@/utils";
import "./index.css";
import { getBuyMember, paymentQuery } from "@/api/commodity";
const DialogBuyProduction = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  const productionInfo = useRef(null);

  // 支付状态轮询
  const paymentQueryTimerRef = useRef(null);
  // 是否选择
  const [payMethodInfo, setPayMethodInfo] = useState({
    type: "",
    resultUrl: "",
  });

  useImperativeHandle(ref, () => {
    return {
      getPage,
    };
  });

  /**
   * @description: 弹框展示
   * @return {*}
   * @author: jinglin.gao
   */
  const getPage = (data) => {
    setPageState(true);
    productionInfo.current = data;
    console.log(productionInfo.current, "productionInfo.current ");
  };

  /**
   * @description: 弹框隐藏
   * @return {*}
   * @author: jinglin.gao
   */

  const hidePage = () => {
    setPageState(false);

    setPayMethodInfo({
      type: "",
      resultUrl: "",
    });

    // 清除定时器
    if (paymentQueryTimerRef.current) {
      clearInterval(paymentQueryTimerRef.current);
    }
    productionInfo.current = null;
    paymentQueryTimerRef.current = null;
  };

  /**
   * @description: 轮询查询支付状态
   * @return {*}
   * @author: jinglin.gao
   */
  const paymentQueryState = async (orderNo) => {
    try {
      let params = {
        orderNo,
      };
      let res = await paymentQuery(params);
      if (res.result === 1) {
        messageFn({
          type: "success",
          content: "充值成功,感谢您的惠顾，可前往个人信息页面查询提问次数。",
        });

        clearInterval(paymentQueryTimerRef.current);
        hidePage();
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 选择支付方式
   * @return {*}
   * @author: jinglin.gao
   */
  const choosePayMethod = async (type) => {
    try {
      let data = {
        memberId: productionInfo.current.memberId,
        payType: 20,
      };
      let res = await getBuyMember(data);
      if (res.code === 200) {
        const {
          result: { qrCode, orderNo },
        } = res;

        setPayMethodInfo({
          resultUrl: qrCode,
          type,
        });

        paymentQueryTimerRef.current = setInterval(() => {
          paymentQueryState(orderNo);
        }, 1000);
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp">
            <div className="custom_dialog-head">
              <span className="title">购买商品</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <p className="product_info">
                您选择的商品为
                <span className="active">
                  {productionInfo.current.cardName}
                </span>
                ,共计
                <span className="active">
                  {productionInfo.current.limitCount === -1
                    ? "无限"
                    : productionInfo.current.limitCount}
                </span>
                次， 金额为
                <span className="active">{productionInfo.current.amount}</span>
                元。
              </p>

              <div className="pay_method">
                {/* <button
                  onClick={() => choosePayMethod("wx")}
                  className="btn-shine"
                >
                  <span className="pay_box">
                    <img className="payIcon" src={wxPay} alt="" />
                    微信支付
                  </span>
                </button> */}

                <button
                  onClick={_.debounce(() => {
                    choosePayMethod("ali");
                  }, 500)}
                  className="btn-shine"
                >
                  <span className="pay_box">
                    <img className="payIcon" src={aliPay} alt="" />
                    支付宝支付
                  </span>
                </button>
              </div>

              {payMethodInfo.resultUrl ? (
                <div className="qrCode_box">
                  <p className="pay_title">
                    {payMethodInfo.type === "wx" ? "微信支付" : "支付宝支付"}
                  </p>
                  <QRCode
                    id="qrCode"
                    value={payMethodInfo.resultUrl}
                    size={200} // 二维码的大小
                    fgColor="#000000" // 二维码的颜色
                  />
                </div>
              ) : (
                ""
              )}
            </div>
          </div>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default DialogBuyProduction;
