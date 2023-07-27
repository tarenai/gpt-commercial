/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-08 10:12:10
 */
import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useRef,
} from "react";

import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";

const DialogBuyProduction = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);

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
  const getPage = () => {
    setPageState(true);
  };

  /**
   * @description: 弹框隐藏
   * @return {*}
   * @author: jinglin.gao
   */

  const hidePage = () => {
    setPageState(false);
  };

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp">
            <div className="custom_dialog-head">
              <span className="title">个人信息</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content"></div>
          </div>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default DialogBuyProduction;
