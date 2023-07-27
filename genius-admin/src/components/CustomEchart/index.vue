<template>
  <div :id="domId" class="customEchart"></div>
</template>

<script>
import * as echarts from "echarts";
export default {
  props: {
    option: {
      required: false,
      default: () => {
        return {
          title: {
            text: "ECharts 入门示例",
          },
          tooltip: {},
          legend: {
            data: ["销量"],
          },
          xAxis: {
            data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
          },
          yAxis: {},
          series: [
            {
              name: "销量",
              type: "bar",
              data: [5, 20, 36, 10, 10, 20],
            },
          ],
        };
      },
    },
  },
  watch: {
    option: {
      handler() {
        this.initEchart();
      },
      deep: true,
    },
  },
  data() {
    return {
      domId: "customEchart" + Math.random() * 1000,
      myEchart: null, // 实例对象  可以是组件的实例对象或者是组件的实例化方法的返回值。 这个组件的实例化方法应该在页面加载完成后自动调用一次。 这个组件还可以在子例程中的位置或方法中给出。 还可以使用一些简单的例子来展示你的实现方式。 如果组件在页面中未加载，
    };
  },
  methods: {
    // 初始化echarts
    initEchart() {
      if (this.myEchart) {
        this.myEchart.dispose();
      }
      let targetDom = document.getElementById(this.domId);
      this.myEchart = echarts.init(targetDom);
      this.myEchart.setOption(this.option, true);
    },
  },

  mounted() {
    this.initEchart();
  },
};
</script>

<style lang="scss" scoped>
.customEchart {
  width: 100%;
  height: 100%;
}
</style>
