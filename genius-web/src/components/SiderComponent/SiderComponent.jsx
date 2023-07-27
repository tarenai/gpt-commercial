/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-17 15:31:24
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2022-10-12 15:19:52
 */
import React from "react";
import { Layout, Menu } from "antd";
import { NavLink } from "react-router-dom";
const { Sider } = Layout;

const SiderComponent = (props) => {
  const { menu: menuList, selectedKey, changeSelectedKey } = props;
  /**
   * @description: 递归向左侧菜单添加跳转
   * @param {*} data
   * @return {*}
   * @author: jinglin.gao
   */
  const deepMenuItems = (data) => {
    if (!data || !data.length) return;
    data.forEach((item) => {
      if (item.children && item.children.length) {
        deepMenuItems(item.children);
      } else {
        item.label = <NavLink to={item.path}>{item.key}</NavLink>;
      }
    });
  };
  deepMenuItems(menuList);

  console.log(menuList, "menuList");

  /**
   * @description: 左侧菜单选中时保存当前菜单的key到sessionStorage中
   * @return {*}
   * @author: jinglin.gao
   */
  const leftMenuOnSelect = ({ key }) => {
    window.sessionStorage.setItem("sessionStorageLeftMenuSelectedKey", key);
    changeSelectedKey(key);
  };

  return (
    <>
      <Sider width={200} className="site-layout-background">
        <Menu
          onSelect={leftMenuOnSelect}
          selectedKeys={[selectedKey]}
          theme="dark"
          mode="inline"
          style={{
            height: "100%",
          }}
          items={menuList}
        ></Menu>
      </Sider>
    </>
  );
};

export default SiderComponent;
