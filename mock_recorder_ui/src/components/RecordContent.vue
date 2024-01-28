<template>
  <a-collapse v-model:activeKey="activeKey">
    <a-collapse-panel v-for="item in recordContent" :key="String(item.id)" :header="item.desc + ': ' + item.type">
      <div>
        <div class="button">
          <a-button @click="onDownload(item)">Download</a-button>
        </div>
        <a-divider>Json Data</a-divider>
        <p v-html="item.htmlShow"></p>
      </div>
    </a-collapse-panel>
  </a-collapse>
</template>

<script>
import { notification } from 'ant-design-vue';

export default {
  data() {
    return {
      activeKey: ''
    }
  },
  props: ["recordContent"],
  methods: {
    onDownload(item) {
      let fileName = item.type + "_" +  new Date().getTime() + ".json"
      if (typeof navigator.msSaveBlob === 'function') {
        // 支持Microsoft Edge和Internet Explorer浏览器
        navigator.msSaveBlob(new Blob([item.content], { type: 'text/plain;charset=utf-8;' }), fileName);
      } else if (typeof URL === 'function' && typeof HTMLCanvasElement.prototype.toBlob === 'function') {
        // 支持Chrome、Firefox和Safari浏览器
        var link = document.createElement('a');
        link.download = fileName;
        link.href = URL.createObjectURL(new Blob([item.content], { type: 'text/plain;charset=utf-8;' }));
        link.style.display = 'none';
        document.body.appendChild(link);
        link.click();
      } else {
        notification["error"]({
          message: 'save fail',
          description: 'your browser not support',
        });
      }
    }
  }
}
</script>

<style scoped>
.button {
  text-align: right;
  width: 100%
}
</style>