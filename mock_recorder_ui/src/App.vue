<template>  
  <div class="container" :style="{ height: windowHeight + 'px' }">  
    <div class="top-div" :style="{ height: topDivHeight + 'px' }">
      <Search 
        :height="topDivHeight" 
        @onGetRecords="onGetRecords"></Search>
    </div>  
    <div class="bottom-div" :style="{ height: bottomDivHeight + 'px' }">  
      <div class="left-div" :style="{ height: bottomDivHeight + 'px' }">
        <RecordList 
          :height="bottomDivHeight" 
          :recordList="recordList" 
          :selectedDate="selectedDate" 
          @selectRow="onSelectRow"></RecordList>
      </div>
      <div class="right-div" :style="{ height: bottomDivHeight + 'px' }">
        <RecordContent :recordContent="recordContent"></RecordContent>
      </div>
    </div>
  </div>
</template>
  
<script>
import Search from "./components/Search.vue"
import RecordList from "./components/RecordList.vue"
import RecordContent from "./components/RecordContent.vue"
export default {  
  name: "App",  
  components: {
    Search,
    RecordList,
    RecordContent,
  },
  data() {  
    return {  
      windowHeight: 0,
      bottomDivHeight: 0,
      selectedDate: {},
      recordList: [],
      recordContent: []
    };  
  },  
  mounted() {  
    this.windowHeight = window.innerHeight
    this.topDivHeight = this.windowHeight * 0.1
    this.bottomDivHeight = this.windowHeight * 0.9
  },  
  methods: {
    onGetRecords(records) {
      this.recordList = records
    },
    onSelectRow(row) {
      let paramInfo = row.paramInfo
      let i = 1
      var recordContent = []
      for (let item of paramInfo.requestParams) {
        recordContent.push(this.parseParam(item, "Parameter" + i, i++))
      }
      if (paramInfo.returnValue != null) {
        recordContent.push(this.parseParam(paramInfo.returnValue, "ReturnValue", i++))
      }
      if (paramInfo.exception != null) {
        recordContent.push(this.parseParam(paramInfo.exception, "Exception", i++))
      }
      this.recordContent = recordContent
      console.log(this.recordContent)
    },
    parseParam(param, desc, i) {
      return {
        "id": i,
        "type": param.type,
        "content": param.value,
        "desc": desc
      }
    }
  }
};  
</script>  
  
<style scoped>  
.container {  
  display: flex;  
  flex-direction: column;  
}  
.bottom-div {  
  flex-grow: 1;  
  display: flex;  
  justify-content: space-between;  
}  
  
.left-div {  
  flex-basis: 60%;  
}  
.right-div {  
  flex-basis: 40%;  
}  
</style>