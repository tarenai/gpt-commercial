/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-15 13:46:14
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-19 15:02:59
 */

// 注册用户趋势图
export const registerUserTrendOption = {
  xAxis: {
    type: 'category',
    name: "日期", // 坐标轴名称
    boundaryGap: false,
    data: ['1月', '2月', '3月', '4月', '5月', '6月']
  },
  color: ['#A4E0F7'],
  grid: {
    left: "2%",
    right: "6%",
    bottom: "25%",
    top: "18%",
    containLabel: true,
  },
  yAxis: {
    name: "人数", // 坐标轴名称
    type: 'value',
  },
  dataZoom: {
    show: true,
    realtime: true,
    brushSelect: false, //取消拖动手柄
    start: 0,
    end: 100,
    height: 24,
    handleSize: "80%",
    backgroundColor: "#F5F7FA",
    textStyle: {
      color: "#fff",
      fontSize: "14",
    },
  },
  series: [{
    smooth: 0.6,
    data: [1200, 1300, 1400, 900, 850, 650],
    type: 'line',
    itemStyle: {
      normal: {
        label: {
          formatter: "{c}人",
          show: true,
          position: "top",
          textStyle: {
            fontWeight: "bolder",
            fontSize: "12",
          },
        },
      },
    },
    areaStyle: {}
  }]
}

// 用户提问趋势图

export const qATrendOption = {
  tooltip: {
    show: true,
    formatter: "{b0}: {c0} 次",
  },
 
  grid: {
    left: "2%",
    right: "6%",
    bottom: "25%",
    top: "18%",
    containLabel: true,
  },
  dataZoom: {
    show: true,
    realtime: true,
    brushSelect: false, //取消拖动手柄
    start: 0,
    end: 100,
    height: 24,
    handleSize: "80%",
    backgroundColor: "#F5F7FA",
    textStyle: {
      color: "#fff",
      fontSize: "14",
    },
  },
  xAxis: {
    type: "category",
    name: "日期", // 坐标轴名称
    data: [],
    axisTick: {
      lineStyle: {
        color: "#000",
      },
    },
    axisLine: {
      show: true,
      lineStyle: {
        color: "#000",
        width: 1,
        type: "solid",
      },
    },
  },

  yAxis: {
    name: "次数", // 坐标轴名称
    type: "value",
    splitLine: {
      show: false,
    },
    axisLine: {
      show: true,
      lineStyle: {
        color: "#000",
      },
    },
    axisLabel: {
      //y轴文字的配置
      textStyle: {
        color: "#000",
      },
      formatter: "{value} 次", //y轴的每一个刻度值后面加上‘%’号
    },
  },

  series: [{
    data: [],
    type: "bar",
    showBackground: true,
    backgroundStyle: {
      color: "rgba(180, 180, 180, 0.2)",
    },

    itemStyle: {
      normal: {
        label: {
          formatter: "{c}次",
          show: true,
          position: "top",
          textStyle: {
            fontWeight: "bolder",
            fontSize: "12",
            color: "#000",
          },
        },
      },
    },
  }, ],
}


// 用户支付趋势图

export const userPaymentOption = {
  xAxis: {
    type: 'category',
    name: "日期", // 坐标轴名称
    boundaryGap: false,
    data: ['1月', '2月', '3月', '4月', '5月', '6月']
  },
  color: ['#B9E2A5'],
  grid: {
    left: "5%",
    right: "5%",
    bottom: "25%",
    top: "18%",
    containLabel: true,
  },
  yAxis: {
    name: "人数", // 坐标轴名称
    type: 'value',
    position:"left"
  },
  dataZoom: {
    show: true,
    realtime: true,
    brushSelect: false, //取消拖动手柄
    start: 0,
    end: 100,
    height: 24,
    handleSize: "80%",
    backgroundColor: "#F5F7FA",
    textStyle: {
      color: "#fff",
      fontSize: "14",
    },
  },
  series: [{
    smooth: 0.6,
    data: [1200, 1300, 1400, 900, 850, 650],
    type: 'line',
    itemStyle: {
      normal: {
        label: {
          formatter: "{c}人",
          show: true,
          position: "top",
          textStyle: {
            fontWeight: "bolder",
            fontSize: "12",
          },
        },
      },
    },
    areaStyle: {}
  }]
}

// 成功支付金额趋势图

export const paymentSuccessfulOption = {
  tooltip: {
    show: true,
    formatter: "{b0}: {c0} 元",
  },
 
  grid: {
    left: "5%",
    right: "5%",
    bottom: "25%",
    top: "18%",
    containLabel: true,
  },
  dataZoom: {
    show: true,
    realtime: true,
    brushSelect: false, //取消拖动手柄
    start: 0,
    end: 100,
    height: 10,
    handleSize: "80%",
    backgroundColor: "#F5F7FA",
    textStyle: {
      color: "#fff",
      fontSize: "14",
    },
  },
  xAxis: {
    type: "category",
    name: "日期", // 坐标轴名称
    data: [],
    axisTick: {
      lineStyle: {
        color: "#000",
      },
    },
    axisLine: {
      show: true,
      lineStyle: {
        color: "#000",
        width: 1,
        type: "solid",
      },
    },
  },

  yAxis: {
    name: "金额(元)", // 坐标轴名称
    type: "value",
    splitLine: {
      show: false,
    },
    axisLine: {
      show: true,
      lineStyle: {
        color: "#000",
      },
    },
    axisLabel: {
      //y轴文字的配置
      textStyle: {
        color: "#000",
      },
      formatter: "{value} 元", //y轴的每一个刻度值后面加上‘%’号
    },
  },

  series: [{
    data: [],
    type: "bar",
    showBackground: true,
    backgroundStyle: {
      color: "rgba(180, 180, 180, 0.2)",
    },

    itemStyle: {
      normal: {
        label: {
          formatter: "{c} 元",
          show: true,
          position: "top",
          textStyle: {
            fontWeight: "bolder",
            fontSize: "12",
            color: "#000",
          },
        },
      },
    },
  }, ],
}