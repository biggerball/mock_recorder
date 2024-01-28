<template>
  <a-table
    :columns="columns"
    :data-source="recordList"
    :pagination="{ pageSize: 50 }"
    :scroll="{ y: height * 0.8 }"
    :customRow="(record) => {
      return {
        onClick: (_) => { this.$emit('selectRow', record) }
      }
    }"
  />
</template>

<script>
export default {
  data() {
    return {
      columns: [{
        title: 'TraceId',
        dataIndex: 'traceId'
      }, {
        title: 'ClassName',
        dataIndex: 'className'
      }, {
        title: 'Interfaces',
        dataIndex: 'interfaces'
      }, {
        title: 'MethodName',
        dataIndex: 'methodName'
      }, {
        title: 'CreateTime',
        dataIndex: 'createTime'
      }],
    }
  },
  props: ["height", "recordList"],
}
</script>

<style scoped>
</style>