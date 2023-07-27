/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-05 13:15:13
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-19 09:38:10
 */
import React, { useState, useEffect, useRef } from "react";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { Button } from "antd";
import { getProducts } from "@/api/commodity";
import styles from "./index.module.less";
import "./index.css";
import logo from "../../../public/assets/imgs/logo.svg";
import { Col, Row } from "antd";
import { useHistory } from "react-router-dom";

import PayBox from "./components/PayBox";
const Commodity = () => {
  const history = useHistory();
  const [commodityList, setCommodityList] = useState([]);
  const [selectedCard, setSelectedCard] = useState({});
  const payBoxRef = useRef(null);
  /**
   * @description: 获取商品列表
   * @return {*}
   * @author: jinglin.gao
   */

  const getProductsFn = async () => {
    try {
      let res = await getProducts();
      if (res.code === 200) {
        const { result } = res;
        setCommodityList(result);
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 返回首页
   * @return {*}
   * @author: jinglin.gao
   */
  const backHome = () => {
    history.replace("/ai/home");
  };

  /**
   * @description: 选中商品
   * @return {*}
   * @author: jinglin.gao
   */
  const chooseCard = (data) => {
    setSelectedCard(data);
    payBoxRef.current.getPage(data);
  };

  useEffect(() => {
    getProductsFn();
  }, []);
  return (
    <div className={styles.commodity}>
      <div className="commodity_tools">
        <Button onClick={backHome} icon={<ArrowLeftOutlined />}>
          返回
        </Button>
      </div>
      <p className="title">购买套餐</p>
      <img className="logo" src={logo} alt="" />
      <div className="commodity_content">
        <Row gutter={16}>
          {commodityList.map((v) => (
            <Col key={v.cardName + new Date().getTime()} span={6}>
              <div
                onClick={() => chooseCard(v)}
                className={`commodity_content_animate_card wallet ${
                  v.id === selectedCard.id
                    ? "commodity_content_selected_card"
                    : ""
                }`}
              >
                <div className="overlay"></div>

                <div className="commodity_content_card">
                  <p className="count">
                    {v.limitCount === -1 ? "不限" : v.limitCount} 次
                  </p>
                  <p className="date">{v.cardName}</p>
                  <p className="price">{v.amount} 元</p>
                </div>
              </div>
            </Col>
          ))}
        </Row>
      </div>

      {/* 移动端兼容 */}
      <div className="mobile_commodity_content">
        {commodityList.map((v) => (
          <div
            key={v.cardName + new Date().getTime()}
            onClick={() => chooseCard(v)}
            className={`commodity_content_animate_card wallet ${
              v.id === selectedCard.id ? "commodity_content_selected_card" : ""
            }`}
          >
            <div className="overlay"></div>

            <div className="commodity_content_card">
              <p className="count">
                {v.limitCount === -1 ? "不限" : v.limitCount} 次
              </p>
              <p className="date">{v.cardName}</p>
              <p className="price">{v.amount} 元</p>
            </div>
          </div>
        ))}
      </div>

      <PayBox ref={payBoxRef}></PayBox>
    </div>
  );
};

export default Commodity;
