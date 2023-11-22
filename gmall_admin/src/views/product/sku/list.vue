<template>
  <div class="app-container">

    <!--三级下拉列表-->
    <CategorySelector @listenOnSelect="getSpuList"/>

    <!--    选择spu-->
    <el-form :inline="true" class="demo-form-inline">

      <el-form-item label="选择spu">
        <el-select
          v-model="spuId"
          placeholder="请选择"
          @change="handleSpuChanged">
          <el-option
            v-for="spuInfo in spuInfoList"
            :key="spuInfo.id"
            :label="spuInfo.spuName"
            :value="spuInfo.id"/>
        </el-select>
      </el-form-item>
    </el-form>

    <!-- banner列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="数据正在加载......"
      border
      fit
      highlight-current-row>

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="id" label="SKU ID" width="100" align="center"/>

      <el-table-column label="banner" width="160" align="center">
        <template slot-scope="scope">
          <div class="info">
            <div class="pic">
              <img :src="scope.row.skuDefaultImg" alt="scope.row.title" style="width: 50px;">
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="skuName" label="名称" align="center"/>

      <el-table-column prop="price" label="价格" width="70"/>

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isSale === 0" size="mini" icon="el-icon-edit" @click="onSale(scope.row.id)">上架
          </el-button>
          <el-button v-if="scope.row.isSale === 1" type="warning" size="mini" icon="el-icon-delete"
                     @click="cancelSale(scope.row.id)">下架
          </el-button>
          <el-button type="danger" size="mini" icon="el-icon-delete" @click="deleteSku(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import sku from '@/api/product/sku'
import spu from '@/api/product/spu'
import CategorySelector from '@/views/components/CategorySelector'

export default {
  components: { CategorySelector },
  data() {
    return {
      listLoading: false, // 数据是否正在加载
      list: [], // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象
      multipleSelection: [], // 批量选择中选择的记录列表,
      spuInfoList: [], //spu选择列表

      // spu所属分类
      // 属性所属分类
      category1Id: 0,
      category2Id: 0,
      category3Id: 0,
      categoryLevel: 1,
      spuId: ''
    }
  },

  methods: {
    async handleSpuChanged(spuId) {
      console.log(spuId)
      this.listLoading = true
      const res = await sku.getSkuBySpuId(spuId)
      this.list = res.data
      this.listLoading = false
    },
    async fetchSpuSelectListData() {
      try {
        const result = await spu.getAllSpuInfoByCategory3Id(this.category3Id)
        this.spuInfoList = result.data
      } catch (e) {
        console.log(e)
      }
    },
    getSpuList(categoryId = 1, categoryLevel) {
      this.categoryLevel = categoryLevel
      if (categoryLevel === 1) {
        this.category1Id = categoryId
        this.category2Id = 0
        this.category3Id = 0
      }
      if (categoryLevel === 2) {
        this.category2Id = categoryId
        this.category3Id = 0
      }
      if (categoryLevel === 3) {
        this.category3Id = categoryId
      }
      this.searchObj.category3Id = categoryId
      // 查询数据
      // this.fetchData(1)
      this.fetchSpuSelectListData()
    },

    deleteSku(skuId) {
      //TODO: 删除sku
    },
    onSale(skuId) {
      sku.onSale(skuId).then(response => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.handleSpuChanged(this.spuId)
      })
    },

    cancelSale(skuId) {
      sku.cancelSale(skuId).then(response => {
        this.$message({
          type: 'success',
          message: '操作成功!'
        })
        this.handleSpuChanged(this.spuId)
      })
    },

    // 重置查询表单
    resetData() {
      console.log('重置查询表单')
      this.searchObj = {}
      this.fetchData()
    }
  }
}
</script>

