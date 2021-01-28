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
                      placeholder="输入标题搜索..."
                      @on-search="handleSearchNotice()"
                    >
                    </Input>
                  </FormItem>
                </Form>
              </Col>
              <Col span="8" class="dnc-toolbar-btns">
                <ButtonGroup class="mr3">
                  <!-- <Button
                    class="txt-danger"
                    icon="md-trash"
                    title="删除"
                    @click="handleBatchCommand('delete')"
                  ></Button> -->

                  <Button icon="md-refresh" title="刷新" @click="handleRefresh"></Button>
                </ButtonGroup>
                <Button
                  icon="md-create"
                  type="primary"
                  @click="handleShowCreateWindow"
                  title="新增药材"
                >新增药材</Button>
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

          <template slot-scope="{ row }" slot="product_img">
            <viewer>
              <Avatar shape="square" :src="row.image" />
            </viewer>
          </template>
          <template slot-scope="{ row }" slot="product_addtime">
            {{ formateDate(new Date(row.createTime),"Y-M-D h:min:s") }}
          </template>
        
          
          <template slot-scope="{ row }" slot="product_status">
              {{ row.status==1?'上架':'下架' }}
          </template>
          <template slot-scope="{ row }" slot="product_price">
              ${{ row.price }}
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
            <Tooltip placement="top" content="药材处理" :delay="1000" :transfer="true">
              <Button
                type="info"
                size="small"
                shape="circle"
                icon="md-create"
                @click="handleEdit2(row)"
              ></Button>
            </Tooltip>
            <Tooltip placement="top" content="编辑" :delay="1000" :transfer="true">
              <Button
                type="primary"
                size="small"
                shape="circle"
                icon="md-create"
                @click="handleEdit(row)"
              ></Button>
            </Tooltip>
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
            <FormItem label="标题" prop="name">
              <Input autocomplete="off"  v-model="formModel.fields.name" placeholder="药材名称" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="价格" prop="price">
              <Input type="number" v-model="formModel.fields.price" placeholder="药材价格" />
            </FormItem>
          </Col>
        </Row>


        <Row :gutter="16">
          <Col span="12">
            <FormItem label="库存" prop="stock">
              <Input type="number" v-model="formModel.fields.stock" placeholder="库存数量" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="拼音" prop="pyName">
              <Input  v-model="formModel.fields.pyName" placeholder="药材拼音" />
            </FormItem>
          </Col>
        </Row>
       

        <FormItem class="notice"  label="介绍内容" label-position="top">
         <Input type="textarea" v-model="formModel.fields.detail" :rows="4" placeholder="介绍内容" />
        </FormItem>

        <FormItem class="notice"  label="药材图片" label-position="top">
         <div class="form-group">
          <label class="control-label">上传图片</label>
          <div class="control-form">
            <p class="help-block">(建议图片格式为：JPEG/BMP/PNG/GIF，大小不超过2M)</p>
            <ul class="upload-imgs">
              <li v-if='formModel.fields.image==""'>
                <a class="add"><i class="iconfont icon-plus"></i><p>点击上传</p></a>
                <input type="file" class="upload" @change="addImg" ref="inputer" multiple accept="image/png,image/jpeg,image/bmp,image/gif,image/jpg"/>
              </li>

              <li v-else>
              <p class="img">
                  <span>
                    <img :src="formModel.fields.image">
                  </span>
                <a class="close" @click="delImg()">×</a>
              </p>
            </li>
            </ul>
            <ul class="upload-imgs">

            </ul>
          </div>
        </div>
        </FormItem>
      </Form>
      <div class="demo-drawer-footer">
        <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitUser" v-show="formModel.mode!='show'">保 存</Button>
        <Button style="margin-left: 8px" icon="md-close" @click="formModel.opened = false">取 消</Button>
      </div>
    </Drawer>
    <Drawer
      title="药材处理"
      v-model="variantModel.opened"
      width="750"
      :mask-closable="false"
      :mask="true"
      :styles="styles"
    >
      <!--添加修改弹出窗口-->
      <Form :model="variantModel.fields" ref="formNotice2" :rules="variantModel.rules" label-position="top">
        <Row :gutter="16">
          <Col span="12">
            <FormItem label="名称" prop="name" >
              <Input   v-model="variantModel.fields.name" placeholder="处理名称" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="价格" prop="price">
              <Input type="number"  v-model="variantModel.fields.price" placeholder="收费价格" />
            </FormItem>
          </Col>
        </Row>

       
       <Card >
        <dz-table
          :totalCount="stores.variant.query.totalCount"
          :pageSize="stores.variant.query.pageSize"
          :currentPage="stores.variant.query.pageNum"
          :showPage="false"
        >
          <!-- *******************************信息显示********************************  -->
          <Table
            slot="table"
            ref="tables"
            :border="false"
            size="small"
            :highlight-row="true"
            :data="stores.variant.data"
            :columns="stores.variant.columns"
          >
          </Table>
        </dz-table>
      </Card>


        
      </Form>
      <div class="demo-drawer-footer">
        <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitUser" v-show="variantModel.mode!='show'">保 存</Button>
        <Button style="margin-left: 8px" icon="md-close" @click="variantModel.opened = false">取 消</Button>
      </div>
    </Drawer>
   
  </div>
</template>

<script>
import DzTable from "_c/tables/dz-table.vue";
import {
  getProductList,
  addProduct,
  batchUpdateSellStatus,
  deleteProduct,
  updateProduct,
  addFile,
} from "@/api/goods";
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
        title: "创建药材",
        mode: "create",
        selection: [],
        fields: {
          categoryId:1,
          detail:"",
          image:"",
          name:"",
          price:"",
          status:"1",
          stock:"",
          id: 0,
          pyName:""
        },

        rules: {
          name: [{ type: "string",required: true, message: "请输入药材名称" }],
          pyName: [{ type: "string",required: true, message: "请输入药材拼音" }],
          price: [{ type: "number", required: true, message: "请输入药材价格" }],
          stock: [{ type: "number", required: true, message: "请输入库存数量" }],
        },
      },
      variantModel: {
        opened: false,
        title: "创建订单",
        mode: "create",
        selection: [],
        fields: {
          name: "",
          productId: "",
          price: "",
        },

        rules: {
          name: [{ type: "string",required: true, message: "请输入处理名称" }],
          price: [{ type: "number",required: true, message: "请输入处理收费价格" }],
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
            { title: "商品名称", key: "name", sortable: true },
            { title: "介绍", key: "detail",  },
            { title: "图片", key: "image", slot: "product_img" },
            { title: "价格", key: "price", slot: "product_price" },
            { title: "商品库存", key: "stock" },
            { title: "状态", key: "status", slot: "product_status" },
            { title: "添加时间", key: "createTime", slot: "product_addtime" },
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
        variant:{
          query:{
            pageNum: 1,
            pageSize: 50,
            totalCount: 0,
          },
          columns: [
            { title: "序号", type: "index", width: 50 },
            { title: "药材名称", key: "productName" },
            { title: "单价", key: "unitPrice",  },
            { title: "数量", key: "quantity" },
            { title: "处理类型", key: "typename" },
          ],
          data:[]
        }
      },
      styles: {
        height: "calc(100% - 55px)",
        overflow: "auto",
        paddingBottom: "53px",
        position: "static",
      },
    };
  },
  computed: {
    formTitle() {
      if (this.formModel.mode === "create") {
        return "创建药材";
      }
      if (this.formModel.mode === "edit") {
        return "编辑药材";
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
    //格式话介绍文字
    formateDetail(txt) {
      console.log(txt);
      var str = "暂无介绍";
      if (txt == null) return str;
      str = txt.length > 10 ? str.substring(0, 10) + "..." : txt;
      return str;
    },
    delImg() {
      this.formModel.fields.image = "";
    },
    addImg(event) {
      let inputDOM = this.$refs.inputer;
      // 通过DOM取文件数据
      this.fil = inputDOM.files;
      for (let i = 0; i < this.fil.length; i++) {
        let size = Math.floor(this.fil[i].size / 1024);
        if (size > 2 * 1024 * 1024) {
          alert("请选择2M以内的图片！");
          return false;
        }
        let formData = new FormData();
        formData.append("file", this.fil[i]);
        addFile(formData).then(res => {
            if(res.data.status=="200")
            {
                this.$Message.success("上传成功");
                // this.img = this.imageUrl + res.data.data;
                this.formModel.fields.image = res.data.data;
            }
            else
            {
                this.$Message.warning("上传失败");
            }
        });
      }
    },

    initdata() {
      var params=new FormData();
      params.append("pageNum",this.stores.goods.query.pageNum);
      params.append("pageSize",this.stores.goods.query.pageSize);
      params.append("keyword",this.stores.goods.query.keyword);
      getProductList(params).then((res) => {
        console.log(res.data.data);
        if (res.data.status == 200) {
          var data = res.data.data;
          var list=data.list;
          list.forEach(item=>{
            if(item.detail==null || item.detail==""){
              item.detail="暂无介绍";
            }else{
              item.detail = item.detail.length > 10 ? item.detail.substring(0, 10) + "..." : txt;
            }
          });
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
    // 药材处理
    handleEdit2(row) {
     this.variantModel.opened = true;
     this.$refs["formNotice2"].resetFields();
      //获取列表数据信息
      
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
    //  提交保存
    handleSubmitUser() {
      var that=this;
      this.$refs["formNotice"].validate((valid) => {
        if(this.formModel.fields.image==""){
          this.$Message.error("请上传药材图片");
          return;
        }
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

      addProduct(params).then(res => {
        console.log(res.data);
          if (res.data.status === 200) {
              this.$Message.success(res.data.msg);
              this.handleCloseFormWindow();
              this.handleRefresh();
          } else {
              this.$Message.warning(res.data.message);
          }
      });
    },
    //提交修改
    doEdit() {
      var params=this.formModel.fields;

      updateProduct(params).then(res => {
        console.log(res.data);
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
        this.formModel.fields.name=data.name;
        this.formModel.fields.detail=data.detail;
        this.formModel.fields.image=data.image;
        this.formModel.fields.price=data.price;
        this.formModel.fields.stock=data.stock;
        this.formModel.fields.pyName=data.pyName;
        console.log(this.formModel.fields);
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
      var formdata=new FormData();
      formdata.append("id",ids);
      deleteProduct(formdata).then(res => {
        if (res.data.status === 200) {
          this.$Message.success(res.data.msg);
          this.formModel.selection = [];
          this.handleRefresh();
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
