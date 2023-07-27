/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-11-06 13:31:06
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2022-11-07 11:05:33
 */
// 引入createStore 创建store
import { legacy_createStore as createStore } from "redux";

// 引入reducer
import home_reducer from "./reducers/home_reducer";

// legacy_createStore 接收reducer作为参数
const store = createStore(home_reducer);
export default store;
