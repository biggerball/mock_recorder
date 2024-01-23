<template>
    <div class="searchContainer">
        <a-form layout="inline">
            <a-form-item label="TraceId" name="traceId">
                <a-input v-model:value="parameter.traceId"/>
            </a-form-item>
            <a-form-item label="ClassName" name="className">
                <a-input v-model:value="parameter.className"/>
            </a-form-item>
            <a-form-item label="MethodName" name="methodName">
                <a-input v-model:value="parameter.methodName" />
            </a-form-item>
            <a-form-item label="DateRange" name="dateRange">
                <a-range-picker @change="changeDate" v-model:value="parameter.dateRange" show-time/>
            </a-form-item>

            <a-form-item>
            <a-button type="primary" html-type="submit" @click="search">Search</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>

<script>
import axios from "axios"
import { notification } from 'ant-design-vue';

export default {
    data() {
        return {
            parameter: {
                traceId: '',
                className: '',
                methodName: '',
                fromTime: '',
                toTime: ''
            }
        }
    },
    methods: {
        search() {
            let body = {
                traceId: this.parameter.traceId,
                className: this.parameter.className,
                methodName: this.parameter.methodName,
                fromTime: this.parameter.fromTime,
                toTime: this.parameter.toTime
            }
            axios.post('http://localhost:10086/db/read', body, {})
            .then((res) => {
                if (res.data.code == 0) {
                    this.$emit("onGetRecords", res.data.data)
                } else {
                    notification["error"]({
                        message: 'search fail',
                        description: res.data.errMsg,
                    });
                }
            })
            .catch((err) => {
                console.log(err)
                notification["error"]({
                    message: 'search fail',
                    description: 'netWork error',
                });
            })
        },
        changeDate(_, dateString) {
            this.parameter.fromTime = dateString[0]
            this.parameter.toTime = dateString[1]
        }
    },
    props: ["height"]
}
</script>

<style scoped>
    .searchContainer {
        display: flex; /* 设置容器为flex布局 */
        justify-content: center; /* 水平居中 */
        align-items: center; /* 垂直居中 */
        height: 100%
    }
</style>