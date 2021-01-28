<template>
  <div>
    <Card>
      <dz-table
        :totalCount="stores.consumable.query.totalCount"
        :pageSize="stores.consumable.query.pageSize"
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
                      type="text"
                      search
                      :clearable="true"
                      v-model="stores.consumable.query.kw"
                      placeholder="输入标题名搜索..."
                      @on-search="handleSearchConsumable()"
                    >
                      <Select
                        slot="prepend"
                        v-model="stores.consumable.query.isDeleted"
                        @on-change="handleSearchConsumable"
                        placeholder="删除状态"
                        style="width:60px;"
                      >
                        <Option
                          v-for="item in stores.consumable.sources.isDeletedSources"
                          :value="item.value"
                          :key="item.value"
                        >{{item.text}}</Option>
                      </Select>
                    </Input>
                  </FormItem>

                </Form>
              </Col>
              <Col span="8" class="dnc-toolbar-btns">
                <ButtonGroup class="mr3">
                  <Button
                    class="txt-danger"
                    icon="md-trash"
                    title="删除"
                    @click="handleBatchCommand('delete')"
                  ></Button>
                  <Button
                    class="txt-success"
                    icon="md-redo"
                    title="恢复"
                    @click="handleBatchCommand('recover')"
                  ></Button>
                  <Button icon="md-refresh" title="刷新" @click="handleRefresh"></Button>
                </ButtonGroup>
                <Button
                  icon="md-create"
                  type="primary"
                  @click="handleShowCreateWindow"
                  title="新增类型"
                >新增类型</Button>
              </Col>
            </Row>
          </section>
        </div>
        <Table
          slot="table"
          ref="tables"
          :border="false"
          size="small"
          :highlight-row="true"
          :data="stores.consumable.data"
          :columns="stores.consumable.columns"
          @on-select="handleSelect"
          @on-selection-change="handleSelectionChange"
          @on-refresh="handleRefresh"
          :row-class-name="rowClsRender"
          @on-page-change="handlePageChanged"
          @on-page-size-change="handlePageSizeChanged"
          @on-sort-change="handleSortChange"
        >
          <template slot-scope="{row,index}" slot="state">
            <span>{{renderState(row.state)}}</span>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Poptip confirm :transfer="true" title="确定要删除吗?" @on-ok="handleDelete(row)">
              <Tooltip placement="top" content="删除" :delay="1000" :transfer="true">
                <Button type="error" size="small" shape="circle" icon="md-trash"></Button>
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
      width="400"
      :mask-closable="false"
      :mask="false"
      :styles="styles"
    >
      <Form
        :model="formModel.fields"
        ref="formConsumable"
        :rules="formModel.rules"
        label-position="left"
      >
        <Row :gutter="16">
          <FormItem label="类型名称" prop="noticeTitle">
            <Input v-model="formModel.fields.goodsTypeName" placeholder="请输入类型名称" />
          </FormItem>
        </Row>

        <FormItem class="notice"  label="商品图片" label-position="top">
         <div class="form-group">
          <label class="control-label">上传图片</label>
          <div class="control-form">
            <p class="help-block">(建议图片格式为：JPEG/BMP/PNG/GIF，大小不超过2M)</p>
            <ul class="upload-imgs">
              <li v-if="imgLen>=1 ? false : true">
                <a class="add"><i class="iconfont icon-plus"></i><p>点击上传</p></a>
                <input type="file" class="upload" @change="addImg" ref="inputer" multiple accept="image/png,image/jpeg,image/bmp,image/gif,image/jpg"/>
              </li>

              <li v-if="imgLen==0 ? false : true">
              <p class="img">
                  <span>
                    <img :src=img>
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
        

        <div class="demo-drawer-footer">
          <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitConsumable">保 存</Button>
          <Button style="margin-left: 8px" icon="md-close" @click="formModel.opened = false">取 消</Button>
        </div>
      </Form>
    </Drawer>
    
  </div>
</template>

<script>
import DzTable from "_c/tables/dz-table.vue";
import {
  getGoodsList,
  createGoods,
  loadOnegoods,
  editGoods,
  deleteOnes,
  getPictureMessage,
  batchCommand,
} from "@/api/goods/goodtype";
import config from '@/config'

export default {
  name: "consumable",
  components: {
    DzTable
  },
  data() {
    return {
      url:config.baseUrl.dev,
                img:"",
                goodsimg:"",
                imageUrl: '',
                formData:new FormData(),
                imgs: {},
                imgLen:0,
      commands: {
        delete: { name: "delete", title: "删除" },
        recover: { name: "recover", title: "恢复" }
      },
      formModel: {
        opened: false,
        title: "添加类型",
        mode: "create",
        selection: [],
        fields: {
          goodsTypeName: "",
          Bgimg: "",
          addPeople: "",
          addTime: "",
        },
        rules: {
          goodsTypeName: [
            { type: "string", required: true, message: "请输入类型名称" }
          ],
          
        }
      },
      
      stores: {
        consumable: {
          query: {
            totalCount: 0,
            pageSize: 20,
            currentPage: 1,
            kw: "",
            time: "",
            isDeleted: 0,
            sort: [
              {
                direct: "DESC",
                field: "ID"
              }
            ]
          },
          sources: {
            isDeletedSources: [
              { value: -1, text: "全部" },
              { value: 0, text: "正常" },
              { value: 1, text: "已删" }
            ]
          },
          columns: [
            { type: "selection", width: 50, key: "handle" },
            { title: "商品类型名称", key: "goodsTypeName" },
            { title: "添加人", key: "addPeople" },
            { title: "添加时间", key: "addTime" },
            {
              title: "操作",
              align: "center",
              width: 150,
              className: "table-command-column",
              slot: "action"
            }
          ],
          data: []
        }
      },
      styles: {
        height: "calc(100% - 55px)",
        overflow: "auto",
        paddingBottom: "53px",
        position: "static"
      }
    };
  },
  computed: {
    formTitle() {
      if (this.formModel.mode === "create") {
        return "创建类型";
      }
      if (this.formModel.mode === "edit") {
        return "编辑类型";
      }
      return "";
    },
    selectedRows() {
      return this.formModel.selection;
    },
    selectedRowsId() {
      return this.formModel.selection.map(x => x.goodsTypeUuid);
    }
  },
  methods: {
    delImg(){
                this.img="";
                this.imgLen--;
            },
            submit(){
                let formData = new FormData();
                for(let key in this.imgs){
                    let name=key.split('?')[0];
                    formData.append('multipartFiles',this.imgs[key],name);
                }
                //console.log(this.formData);


            },
    addImg(event){
                let inputDOM = this.$refs.inputer;
                // 通过DOM取文件数据
                this.fil = inputDOM.files;
                for (let i=0; i < this.fil.length; i++) {
                    let size = Math.floor(this.fil[i].size / 1024);
                    if (size > 2*1024*1024) {
                        alert('请选择2M以内的图片！');
                        return false
                    }
                    this.imgLen++;
                    let formData = new FormData();
                    formData.append('multipartFiles',this.fil[i],this.fil[i].name);
                    getPictureMessage(formData).then(res => {
                        if(res.data.code=="200")
                        {

                            this.$Message.success(res.data.msg);
                            this.img = this.url + res.data.path;
                            this.goodsimg=res.data.path;
                        }
                        else
                        {
                            this.$Message.warning(res.data.msg);
                        }
                    });
                }
            },
    editshow(state, guid) {
      if (
        (state == 0 || state == 3) &&
        this.$store.state.user.userGuid == guid
      ) {
        return true;
      } else return false;
    },
    loadconsumableList() {
      getGoodsList(this.stores.consumable.query).then(res => {
        this.stores.consumable.data = res.data.data;
        this.stores.consumable.query.totalCount = res.data.totalCount;
      });
    },
    handleOpenFormWindow() {
      this.formModel.opened = true;
    },
    handleCloseFormWindow() {
      this.formModel.opened = false;
    },
    handleSwitchFormModeToCreate() {
      this.formModel.mode = "create";
    },
    handleSwitchFormModeToEdit() {
      this.formModel.mode = "edit";
      this.handleOpenFormWindow();
    },
    handleEdit(row) {
      this.handleSwitchFormModeToEdit();
      this.handleResetFormConsumable();
      this.doLoadConsumable(row.goodsTypeUuid);
    },
    
    handleSelect(selection, row) {},
    handleSelectionChange(selection) {
      this.formModel.selection = selection;
    },
    handleRefresh() {
      this.loadconsumableList();
    },
    handleShowCreateWindow() {
      this.handleSwitchFormModeToCreate();
      this.handleOpenFormWindow();
      this.handleResetFormConsumable();
      this.formModel.fields.goodsTypeName="";
      this.img="";
      this.imgLen=0;
    },
    handleSubmitConsumable() {
      let valid = this.validateConsumableForm();
      if (valid) {
        if (this.formModel.mode === "create") {
          this.doCreateConsumable();
        }
        if (this.formModel.mode === "edit") {
          this.doEditConsumable();
        }
      }
    },
    
    handleResetFormConsumable() {
      this.$refs["formConsumable"].resetFields();
    },
    doCreateConsumable() {
      this.formModel.fields.Bgimg=this.goodsimg;
      createGoods(this.formModel.fields).then(res => {
        if (res.data.code === 200) {
          this.$Message.success(res.data.message);
          this.handleCloseFormWindow();
          this.loadconsumableList();
        } else {
          this.$Message.warning(res.data.message);
        }
      });
    },
    doEditConsumable() {
      this.formModel.fields.Bgimg=this.goodsimg;
      editGoods(this.formModel.fields).then(res => {
        if (res.data.code === 200) {
          this.$Message.success(res.data.message);
          this.handleCloseFormWindow();
          this.loadconsumableList();
        } else {
          this.$Message.warning(res.data.message);
        }
      });
    },
    
    validateConsumableForm() {
      let _valid = false;
      this.$refs["formConsumable"].validate(valid => {
        if (!valid) {
          this.$Message.error("请完善表单信息");
        } else {
          _valid = true;
        }
      });
      return _valid;
    },
    doLoadConsumable(Uuid) {
      loadOnegoods({ guid: Uuid }).then(res => {
        console.log(res.data.data);
        this.formModel.fields = res.data.data;
        this.img=this.url+res.data.data.bgimg;
        this.goodsimg=res.data.data.bgimg;
        if(this.img!="")
          this.imgLen++;
      });
    },
    handleDelete(row) {
      this.doDelete(row.goodsTypeUuid);
    },
    doDelete(ids) {
      if (!ids) {
        this.$Message.warning("请选择至少一条数据");
        return;
      }
      deleteOnes(ids).then(res => {
        if (res.data.code === 200) {
          this.$Message.success(res.data.message);
          this.loadconsumableList();
          this.formModel.selection = [];
        } else {
          this.$Message.warning(res.data.message);
        }
      });
    },
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
        }
      });
    },
    doBatchCommand(command) {
      batchCommand({
        command: command,
        ids: this.selectedRowsId.join(",")
      }).then(res => {
        if (res.data.code === 200) {
          this.$Message.success(res.data.message);
          this.loadconsumableList();
          this.formModel.selection = [];
        } else {
          this.$Message.warning(res.data.message);
        }
        this.$Modal.remove();
      });
    },
    handleSearchConsumable() {
      this.loadconsumableList();
    },
    rowClsRender(row, index) {
      if (row.isDeleted) {
        return "table-row-disabled";
      }
      return "";
    },
    handleSortChange(column) {
      this.stores.consumable.query.sort.direction = column.order;
      this.stores.consumable.query.sort.field = column.key;
      this.loadconsumableList();
    },
    handlePageChanged(page) {
      this.stores.consumable.query.currentPage = page;
      this.loadconsumableList();
    },
    handlePageSizeChanged(pageSize) {
      this.stores.consumable.query.pageSize = pageSize;
      this.loadconsumableList();
    },
    renderState(state) {
      if (state == 0) {
        return "否";
      }
      if (state == 1) {
        return "是";
      }
    }
  },
  mounted() {
    this.loadconsumableList();
  }
};
</script>

<style scoped>
  .mec{
    height: 300px;
  }
    #cheshi img{
    width: 100%;
  }

  #cheshi ul{
    width: 93%;
    margin: 0 auto;
    margin-bottom: 30px;
    overflow: hidden;
  }

  #cheshi ul li{
    float: left;
    width: 16.6%;
    text-align: center;
    list-style-type:none;
    font-size: 15px;
  }

  .upload-imgs{margin: 10px 0 30px 0;overflow: hidden;font-size: 0;}
  .upload-imgs li{position: relative;width: 118px;height: 118px;font-size: 14px;display: inline-block;padding: 10px;margin-right: 25px;border: 2px dashed #ccc;text-align: center;vertical-align: middle;overflow: hidden}

  .upload-imgs .add{display: block;background-color: #ccc;color: #ffffff;height: 94px;padding: 8px 0;}
  .upload-imgs .add .iconfont{padding: 10px 0;font-size: 40px;}

  .upload-imgs li .upload{position: absolute;top: -50px;bottom: 0;left: 0;right: 0;width: 118px;height: 145px;}
  .upload-imgs .img{position: relative;width: 94px;height: 94px;line-height: 94px;}
  .upload-imgs .img span{overflow: hidden;width: 94px}
  .upload-imgs .img span img{vertical-align: middle;width: 94px}
  .upload-imgs .img .close{display: none;}
  .upload-imgs li:hover .img .close{display: block;position: absolute;right: -6px;top: -10px;line-height: 1;font-size: 22px;color: #aaa;}

</style>
