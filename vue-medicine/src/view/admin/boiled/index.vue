<template>
  <div>
    <Card>
      <dz-table
        :totalCount="stores.goods.query.totalCount"
        :pageSize="stores.goods.query.pageSize"
        :currentPage="stores.goods.query.pageNum"
        @on-page-change="handlePageChanged"
        @on-page-size-change="handlePageSizeChanged"
        :showPage="false"
      >
      <div slot="searcher">
          <section class="dnc-toolbar-wrap">
            <Row :gutter="16">
              <Col span="24" class="dnc-toolbar-btns">
                <ButtonGroup class="mr3">

                  <Button icon="md-refresh" title="刷新" @click="handleRefresh"></Button>
                </ButtonGroup>
                <Button
                  icon="md-create"
                  type="primary"
                  @click="handleShowCreateWindow"
                  title="添加规则"
                >添加规则</Button>
              </Col>
            </Row>
          </section>
        </div>
        <!-- *******************************信息显示********************************  -->
        <Table
          slot="table"
          ref="tables"
          :border="false"
          size="small"
          :highlight-row="true"
          :data="stores.goods.data"
          :columns="stores.goods.columns"
          @on-select="handleSelect"
          @on-selection-change="handleSelectionChange"
          @on-refresh="handleRefresh"
          :row-class-name="rowClsRender"
          @on-page-change="handlePageChanged"
          @on-page-size-change="handlePageSizeChanged"
          @on-sort-change="handleSortChange"
        >

         
          <template slot-scope="{ row }" slot="product_addtime">
            {{ formateDate(new Date(row.createTime),"Y-M-D h:min:s") }}
          </template>
        
          
          <template slot-scope="{ row }" slot="price">
              <span style="color:red;">${{ row.price}}</span>
          </template>
          
         
          <template slot-scope="{ row }" slot="action">
            <Poptip
              confirm
              :transfer="true"
              title="确定要删除吗?"
              @on-ok="handleDelete(row)"
            >
              <Tooltip placement="top" content="删除" :delay="1000" :transfer="true">
                <Button   type="error" size="small" shape="circle" icon="md-trash"></Button>
              </Tooltip>
            </Poptip>
            <Tooltip placement="top" content="编辑" :delay="1000" :transfer="true">
              <Button
                type="primary"
                size="small"
                shape="circle"
                icon="md-create"
                @click="handleEdit(row)"
              ></Button>
            </Tooltip>
          </template>
        </Table>
      </dz-table>
    </Card>
    <Drawer
      :title="formTitle"
      v-model="formModel.opened"
      width="750"
      :mask-closable="false"
      :mask="true"
      :styles="styles"
    >
<!--添加修改弹出窗口-->
      <Form :model="formModel.fields" ref="formNotice" :rules="formModel.rules" label-position="top">
        <Row :gutter="16">
          <Col span="12">
            <FormItem label="服数" prop="count">
              <Input number  v-model="formModel.fields.count" placeholder="请输入服数" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="价格" prop="price">
              <Input number v-model="formModel.fields.price" placeholder="请输入收费价格" />
            </FormItem>
          </Col>
        </Row>
        
      </Form>
      <div class="demo-drawer-footer">
        <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitUser" v-show="formModel.mode!='show'">保 存</Button>
        <Button style="margin-left: 8px" icon="md-close" @click="formModel.opened = false">取 消</Button>
      </div>
    </Drawer>
   
  </div>
</template>

<script>
import DzTable from "_c/tables/dz-table.vue";
import {
  deleteBoiled,
  addBoiled,
  updateBoiled,
  getBoiledList
} from "@/api/boiled";
import {
  getSystemInfo,
} from "@/api/userAdmin";
import config from "@/config";
import { formateDate } from "@/libs/tools";

export default {
  name: "goods",
  components: {
    DzTable,
  },
  data() {
    return {
      url: config.baseUrl.imgUrl,
      formData: new FormData(),
      imgs: {},
      commands: {
        delete: { name: "delete", title: "删除" },
        recover: { name: "recover", title: "恢复" },
        forbidden: { name: "forbidden", title: "禁用" },
        normal: { name: "normal", title: "启用" },
      },
      formModel: {
        opened: false,
        title: "创建规则",
        mode: "create",
        selection: [],
        fields: {
          count: "",
          price: "",
          id: 0,
        },

        rules: {
          count: [{ type: "number",required: true, message: "请输入煮药服数" }],
          price: [{ type: "number",required: true, message: "请输入收费价格" }],
        },
      },

      stores: {
        //  属性
        goods: {
          query: {
            keyword:"",
            pageNum: 1,
            pageSize: 10,
            totalCount: 0,
          },
          sources: {
            type: [],
          },
          columns: [
            { type: "selection", width: 50, key: "handle" },
            { title: "序号", type: "index", width: 50 },
            { title: "煮药服数", key: "count" },
            { title: "收费价格", key: "price",slot:"price"  },
            {
              title: "操作",
              align: "center",
              // width: 150,
              className: "table-command-column",
              slot: "action",
            },
          ],

          data: [],
        },
      },
      maxadd:10,
      styles: {
        height: "calc(100% - 55px)",
        overflow: "auto",
        paddingBottom: "53px",
        position: "static",
      },
      userId:this.$store.state.adminUser.id
    };
  },
  computed: {
    formTitle() {
      if (this.formModel.mode === "create") {
        return "创建规则";
      }
      if (this.formModel.mode === "edit") {
        return "编辑规则";
      }
      return "";
    },
    selectedRows() {
      return this.formModel.selection;
    },
    selectedRowsId() {
      return this.formModel.selection.map((x) => x.goodsUuid);
    },
  },
  mounted() {
    getSystemInfo().then(res=>{
      console.log(res.data);
      if(res.data.status==200){
        this.maxadd=res.data.data.boidedMax;
      }
    });
    this.initdata();
  },
  methods: {
    //格式话时间
    formateDate(p1, p2) {
      return formateDate(p1, p2);
    },
    //检测登录
    is_havalogin(){
      if(this.userId=='' || this.userId==undefined || this.userId==null){
          this.$Message.warning("请重新登录");
          this.$router.push({name:"login"});
      }
    },
    initdata() {
      this.is_havalogin();
      var params={
        pageNum:this.stores.goods.query.pageNum,
        pageSize:this.stores.goods.query.pageSize
      };
      getBoiledList(params).then((res) => {
        if (res.data.status == 200) {
          var data = res.data.data;
          this.stores.goods.data = data.list;
          this.stores.goods.query.totalCount = data.total;
        } else {
          this.$Message.warning("登录已过期，请重新登录");
          this.$router.push({
            name: 'login'
          })
        }
      });
    },
    handleOpenFormWindow() {
      this.formModel.opened = true;
      //console.log(this.formModel.fields);
    },
    handleCloseFormWindow() {
      this.formModel.opened = false;
    },
    
    handleSwitchFormModeToEdit() {
      this.formModel.mode = "edit";
      this.handleOpenFormWindow();
    },
    //  修改
    handleEdit(row) {
      this.handleSwitchFormModeToEdit();
      this.handleResetFormNotice();
      //根据guid获取本条数据信息
      this.doLoadData(row);
    },
    //勾选数据
    handleSelect(selection) {},
    handleSelectionChange(selection) {
      this.formModel.selection = selection;
    },
    handleRefresh() {
      this.stores.goods.query.pageNum = 1;
      this.initdata();
    },
    //  添加
    handleShowCreateWindow() {
      if(this.stores.goods.query.totalCount>=this.maxadd){
        this.$Message.warning("添加数量已达上限");
        return;
      }
      this.formModel.mode = "create";
      this.handleOpenFormWindow();
      this.handleResetFormNotice();
      //console.log(this.formModel.fields);
    },
    //  提交保存
    handleSubmitUser() {
      var that=this;
      this.$refs["formNotice"].validate((valid) => {
        
        if (!valid) {
          this.$Message.error("请完善表单信息");
        } else {
          if (that.formModel.mode == "create") {
            that.doCreate();
          }
          if (that.formModel.mode == "edit") {
            that.doEdit();
          }
        }
      });
    },
    handleResetFormNotice() {
      this.$refs["formNotice"].resetFields();
    },
    //提交添加
    doCreate() {
      var params=this.formModel.fields;
      addBoiled(params).then(res => {
          if (res.data.status === 200) {
              this.$Message.success(res.data.msg);
              this.handleCloseFormWindow();
              this.handleRefresh();
          } else {
              this.$Message.warning(res.data.msg);
          }
      });
    },
    //提交修改
    doEdit() {
      var params=this.formModel.fields;
      updateBoiled(params).then(res => {
          if (res.data.status === 200) {
              this.$Message.success(res.data.msg);
              this.handleCloseFormWindow();
              this.handleRefresh();
          } else {
              this.$Message.warning(res.data.msg);
          }
      });
    },
    //  通过guid获取当前数据
    doLoadData(rows) {
        var data = rows;
        this.formModel.fields.id=data.id;
        this.formModel.fields.count=data.count;
        this.formModel.fields.price=data.price;
    },
    //  删除公告
    handleDelete(row) {
      this.doDelete(row.id);
    },
    //  通过guid修改删除属性
    doDelete(ids) {
      if (!ids) {
        this.$Message.warning("请选择至少一条数据");
        return;
      }
      deleteBoiled({id:ids}).then(res => {
        if (res.data.status === 200) {
          this.$Message.success(res.data.msg);
          this.handleRefresh();
        } else {
          this.$Message.warning(res.data.msg);
        }
      });
    },
    //  批量操作(删除)
    handleBatchCommand(command) {
      if (!this.selectedRowsId || this.selectedRowsId.length <= 0) {
        this.$Message.warning("请选择至少一条数据");
        return;
      }
      this.$Modal.confirm({
        title: "操作提示",
        content:
          "<p>确定要执行当前 [" +
          this.commands[command].title +
          "] 操作吗?</p>",
        loading: true,
        onOk: () => {
          this.doBatchCommand(command);
        },
      });
    },
    //  具体操作提交数据
    doBatchCommand(command) {
      // batchCommand({
      //   command: command,
      //   ids: this.selectedRowsId.join(",")
      // }).then(res => {
      //   if (res.data.code === 200) {
      //     this.$Message.success(res.data.message);
      //     this.initdata();
      //     this.formModel.selection = [];
      //     this.handleRefresh();
      //   } else {
      //     this.$Message.warning(res.data.message);
      //   }
      //   this.$Modal.remove();
      // });
    },
    handleSearchNotice() {
      this.stores.goods.query.currentPage = 1;
      this.initdata();
    },
    rowClsRender(row, index) {
      if (row.isDeleted) {
        return "table-row-disabled";
      }
      return "";
    },
    //排序
    handleSortChange(column) {
      //console.log(this.stores.goods.query.sort.direct);
      // this.loadPostList();
    },
    handlePageChanged(page) {
      this.stores.goods.query.pageNum = page;
      this.initdata();
    },
    handlePageSizeChanged(pageSize) {
      this.stores.goods.query.pageSize = pageSize;
      this.initdata();
    },
    renderOwnedRoles(item) {
      return item.label;
    },
    //查看详情
    handleAssignRole(row) {
      this.formModel.mode = "show";
      this.handleOpenFormWindow();
      this.handleResetFormNotice();
      this.doLoadData(row);
    },
  },
};
</script>

<style>
.mec {
  height: 300px;
}
#cheshi img {
  width: 100%;
}

#cheshi ul {
  width: 93%;
  margin: 0 auto;
  margin-bottom: 30px;
  overflow: hidden;
}

#cheshi ul li {
  float: left;
  width: 16.6%;
  text-align: center;
  list-style-type: none;
  font-size: 15px;
}

.upload-imgs {
  margin: 10px 0 30px 0;
  overflow: hidden;
  font-size: 0;
}
.upload-imgs li {
  position: relative;
  width: 118px;
  height: 118px;
  font-size: 14px;
  display: inline-block;
  padding: 10px;
  margin-right: 25px;
  border: 2px dashed #ccc;
  text-align: center;
  vertical-align: middle;
  overflow: hidden;
}

.upload-imgs .add {
  display: block;
  background-color: #ccc;
  color: #ffffff;
  height: 94px;
  padding: 8px 0;
}
.upload-imgs .add .iconfont {
  padding: 10px 0;
  font-size: 40px;
}

.upload-imgs li .upload {
  position: absolute;
  top: -50px;
  bottom: 0;
  left: 0;
  right: 0;
  width: 118px;
  height: 145px;
}
.upload-imgs .img {
  position: relative;
  width: 94px;
  height: 94px;
  line-height: 94px;
}
.upload-imgs .img span {
  overflow: hidden;
  width: 94px;
}
.upload-imgs .img span img {
  vertical-align: middle;
  width: 94px;
}
.upload-imgs .img .close {
  display: none;
}
.upload-imgs li:hover .img .close {
  display: block;
  position: absolute;
  right: -6px;
  top: -10px;
  line-height: 1;
  font-size: 22px;
  color: #aaa;
}
</style>
