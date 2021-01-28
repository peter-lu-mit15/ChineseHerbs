<template>
  <div>
    <Card>
      <dz-table
        :totalCount="stores.goods.query.totalCount"
        :pageSize="stores.goods.query.pageSize"
        :currentPage="stores.goods.query.pageNum"
        @on-page-change="handlePageChanged"
        @on-page-size-change="handlePageSizeChanged"
      >
        <div slot="searcher">
          <section class="dnc-toolbar-wrap">
            <Row :gutter="16">
              <Col span="16">
                <Form inline @submit.native.prevent>
                  <FormItem>
                    <Input
                      style="width: 200px;"
                      type="text"
                      search
                      :clearable="true"
                      v-model="stores.goods.query.keyword"
                      placeholder="输入订单号搜索..."
                      @on-search="handleSearchNotice()"
                    >
                    </Input>
                  </FormItem>
                </Form>
              </Col>
              <Col span="8" class="dnc-toolbar-btns">
                <ButtonGroup class="mr3">

                  <Button icon="md-refresh" title="刷新" @click="handleRefresh"></Button>
                </ButtonGroup>
                <!-- <Button
                  icon="md-create"
                  type="primary"
                  @click="handleShowCreateWindow"
                  title="新增药材"
                >新增药材</Button> -->
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
        
          
          <template slot-scope="{ row }" slot="user_role">
              {{ row.role==2?'管理员':'普通用户' }}
          </template>
          
         
          <template slot-scope="{ row }" slot="action">
            <Poptip
              confirm
              :transfer="true"
              title="确定要取消订单吗?"
              @on-ok="handleDelete(row)"
            >
              <Tooltip placement="top" content="取消订单" :delay="1000" :transfer="true">
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

            <Poptip
              confirm
              :transfer="true"
              :disabled="row.orderStatus==50"
              title="确定要提醒客户取药吗?"
              @on-ok="send_email(row)"
            >
              <Tooltip placement="top" content="提醒取药" :delay="1000" :transfer="true">
                <Button :disabled="row.orderStatus==50"   type="warning" size="small" shape="circle" icon="ios-alarm"></Button>
              </Tooltip>
            </Poptip>

           <Tooltip placement="top" content="查看" :delay="1000" :transfer="true">
             <Button
              type="success"
              size="small"
               shape="circle"
               icon="md-list-box"
               @click="handleAssignRole(row)"
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
            <FormItem label="订单编号" >
              <Input   v-model="formModel.fields.orderNo" placeholder="订单编号" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="订单总额" >
              <Input  v-model="formModel.fields.totalPrice" placeholder="订单总额" />
            </FormItem>
          </Col>
        </Row>


        <Row :gutter="16">
          <Col span="12">
            <FormItem label="收货人" >
              <Input  v-model="formModel.fields.receiverName" placeholder="收货人" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="收货电话" >
              <Input  v-model="formModel.fields.receiverMobile" placeholder="收货电话" />
            </FormItem>
          </Col>
        </Row>

        <Row :gutter="16">
          <Col span="24">
            <FormItem label="收货地址">
              <Input  v-model="formModel.fields.receiverAddress" placeholder="收货地址" />
            </FormItem>
          </Col>
          
        </Row>
        <Row :gutter="16">
          <Col span="24">
            <FormItem label="备注信息">
              <Input  v-model="formModel.fields.remarks" placeholder="备注信息" />
            </FormItem>
          </Col>
          
        </Row>
       
       <Card v-for="(item,index) in stores.product.data" v-show="item.length>0">
         <div style="font-weight:bold;color:red;overflow:hidden;">
          <span>配方{{(index*1+1)}}</span>
          <!-- <span>----</span> -->
          <!-- <span>合计：${{get_totalprice(item)}}</span> -->
        </div>
        <dz-table
          :totalCount="item.length"
          :pageSize="stores.product.query.pageSize"
          :currentPage="stores.product.query.pageNum"
          :showPage="false"
        >
          <!-- *******************************信息显示********************************  -->
          <Table
            slot="table"
            ref="tables"
            :border="false"
            size="small"
            :highlight-row="true"
            :data="item"
            :columns="stores.product.columns"
          >
          </Table>
        </dz-table>
      </Card>


        
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
  updateUser
} from "@/api/userAdmin";
import {
  getOrderList,
  deleteOrder,
  detailOrder,
  SendEmailForOrder
} from "@/api/order";
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
      typelist: [
        { title: "片状", key: 1 },
        { title: "块状", key: 2 },
        { title: "粉末", key: 3 },
      ],
      cartdata:[],
      formModel: {
        opened: false,
        title: "创建订单",
        mode: "create",
        selection: [],
        fields: {
          orderNo: "",
          orderStatusName: "",
          receiverAddress: "",
          receiverMobile: "",
          id: 0,
          receiverName: "",
          totalPrice: "",
          userId: "",
          remarks:"",
          orderItemVOList: []
        },

        rules: {
          username: [{ type: "string",required: true, message: "请输入账号" }],
          email: [{ type: "string",required: true, message: "请输入用户邮箱" }],
          firstName: [{ type: "string", required: true, message: "请输入firstName" }],
          lastName: [{ type: "string", required: true, message: "请输入lastName" }],
          state: [{ type: "string", required: true, message: "请输入邮编号" }],
          phone: [{ type: "string", required: true, message: "请输入联系电话" }],
          city: [{ type: "string", required: true, message: "请输入所在城市" }],
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
            { title: "订单编号", key: "orderNo" },
            { title: "订单总额", key: "totalPrice" },
            { title: "订单用户", key: "receiverName",  },
            { title: "联系电话", key: "receiverMobile" },
            { title: "收货地址", key: "receiverAddress" },
            { title: "订单状态", key: "orderStatusName" },
            { title: "创建时间", key: "createTime", slot: "product_addtime" },
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
        product:{
          data:[],
          query: {
            pageNum: 1,
            pageSize: 50,
            totalCount: 0,
          },
          columns: [
            { title: "序号", type: "index", width: 50 },
            { title: "药材名称", key: "productName" },
            { title: "单价", key: "unitPrice",  },
            { title: "数量", key: "quantity" },
            { title: "处理类型", key: "handleName" },
          ],
        }
      },
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
        return "创建订单";
      }
      if (this.formModel.mode === "edit") {
        return "编辑订单";
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
      var params=new FormData();
      params.append("pageNum",this.stores.goods.query.pageNum);
      params.append("pageSize",this.stores.goods.query.pageSize);
      params.append("keyword",this.stores.goods.query.keyword);
      getOrderList(params).then((res) => {
        // console.log(res.data.data);
        if (res.data.status == 200) {
          var data = res.data.data;
          var list=data.list;
          console.log(list);
          this.stores.goods.data = list;
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
      this.formModel.mode = "create";
      this.handleOpenFormWindow();
      this.handleResetFormNotice();
      //console.log(this.formModel.fields);
    },
    send_email(row){
        row.orderStatus=50;
      SendEmailForOrder({orderNo:row.orderNo}).then(res=>{
        if(res.data.status==200){
          this.initdata();
          this.$Message.success("发送成功");
        }else{
          this.$Message.error(res.data.msg);
        }
      });
    },
    //  提交保存
    handleSubmitUser() {
      var that=this;
      that.doEdit();
      // this.$refs["formNotice"].validate((valid) => {
        
      //   if (!valid) {
      //     this.$Message.error("请完善表单信息");
      //   } else {
      //     if (that.formModel.mode == "create") {
      //       that.doCreate();
      //     }
      //     if (that.formModel.mode == "edit") {
      //       that.doEdit();
      //     }
      //   }
      // });
    },
    handleResetFormNotice() {
      this.$refs["formNotice"].resetFields();
    },
    //提交添加
    doCreate() {
      var params=this.formModel.fields;

      // addProduct(params).then(res => {
      //   console.log(res.data);
      //     if (res.data.status === 200) {
      //         this.$Message.success(res.data.msg);
      //         this.handleCloseFormWindow();
      //         this.handleRefresh();
      //     } else {
      //         this.$Message.warning(res.data.message);
      //     }
      // });
    },
    //提交修改
    doEdit() {
      var params=this.formModel.fields;

      updateUser(params).then(res => {
          if (res.data.status === 200) {
              this.$Message.success(res.data.msg);
              this.handleCloseFormWindow();
              this.handleRefresh();
          } else {
              this.$Message.warning(res.data.message);
          }
      });
    },
    //  通过guid获取当前数据
    doLoadData(rows) {
        var data = rows;
        this.formModel.fields.id=data.id;
        this.formModel.fields.orderNo=data.orderNo;
        this.formModel.fields.orderStatusName=data.orderStatusName;
        this.formModel.fields.receiverAddress=data.receiverAddress;
        this.formModel.fields.receiverMobile=data.receiverMobile;
        this.formModel.fields.receiverName=data.receiverName;
        this.formModel.fields.totalPrice=data.totalPrice;
        this.formModel.fields.userId=data.userId;
        this.formModel.fields.remarks=data.remarks;
        this.formModel.fields.orderItemVOList=data.orderItemVOList;
        
        //处理方案分类
        this.stores.product.data=[];
        for(var i=1;i<=10;i++){
          var arr=[];
          for(var j=0;j<data.orderItemVOList.length;j++){
            var item=data.orderItemVOList[j];
            if(item.formula==i){
              arr.push(item);
            }
          }
          this.stores.product.data.push(arr);
        }
        // for(var i=0;i<this.stores.product.data.length;i++){
        //   var arrs=this.stores.product.data[i];
        //   if(arrs.length>0){
        //     arrs.forEach(item=>{
        //       item.typename=this.typelist[item.handle-1].title;
        //     });
        //   }
        // }
        // this.stores.product.data=data.orderItemVOList;
        this.stores.product.query.totalCount = data.orderItemVOList.length;
    },
    //  删除公告
    handleDelete(row) {
      this.doDelete(row.orderNo);
    },
    //  通过guid修改删除属性
    doDelete(ids) {
      if (!ids) {
        this.$Message.warning("请选择至少一条数据");
        return;
      }
      
      var params={
        orderNo:ids
      };
      deleteOrder(params).then(res => {
        if (res.data.status === 200) {
          this.$Message.success(res.data.msg);
          this.handleRefresh();
          this.formModel.selection = [];
        } else {
          this.$Message.warning(res.data.message);
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
