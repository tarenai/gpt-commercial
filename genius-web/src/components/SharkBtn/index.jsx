/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-11 08:38:20
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-11 09:05:13
 */
import React from "react";
import "./index.css";
const index = ({ icon, name, onClick }) => {
  return (
    <button className="sharkBtn" onClick={onClick}>
      <span className="shark_btn-box">
        <img className="shark_btn-img" src={icon} alt="" />
        {name}
      </span>
    </button>
  );
};

export default index;
