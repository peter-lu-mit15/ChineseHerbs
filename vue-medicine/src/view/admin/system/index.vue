<template>
  <div>
    <Card>
        <Form :model="formModel.fields" ref="formNotice" :rules="formModel.rules" label-position="top">
        <Row :gutter="16">
          <Col span="12">
            <FormItem label="系统邮箱" prop="sysEmail">
              <Input autocomplete="off"  v-model="formModel.fields.sysEmail" placeholder="系统邮箱" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="邮箱授权码" prop="sysAuthorization">
              <Input  v-model="formModel.fields.sysAuthorization" placeholder="邮箱授权码" />
            </FormItem>
          </Col>
        </Row>


        <Row :gutter="16">
          <Col span="12">
            <FormItem label="煮药服数（下限）：" prop="boidedMin">
              <Input number v-model="formModel.fields.boidedMin" placeholder="煮药服数" />
            </FormItem>
          </Col>
          <Col span="12">
            <FormItem label="煮药服数（上限）：" prop="boidedMax">
              <Input number v-model="formModel.fields.boidedMax" placeholder="煮药服数" />
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="24">
            <FormItem label="抓药师邮箱（上限）：" prop="graspCount">
              <Input number v-model="formModel.fields.graspCount" placeholder="抓药师邮箱数" />
            </FormItem>
          </Col>
        </Row>
        <div class="demo-drawer-footer" style="text-align:center;">
          <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitUser">保存设置</Button>
          <Button style="margin-left:20px;" icon="md-add" type="info" @click="handleAdd">添加抓药师邮箱</Button>
        </div>

         <Card style="margin-top:20px;">
           <h4 style="margin:10px 2px;">抓药师邮箱列表</h4>
          <dz-table
            :totalCount="stores.email.query.totalCount"
            :pageSize="stores.email.query.pageSize"
            :currentPage="stores.email.query.pageNum"
            :showPage="false"
          >
            <!-- *******************************信息显示********************************  -->
            <Table
              slot="table"
              ref="tables"
              :border="false"
              size="small"
              :highlight-row="true"
              :data="stores.email.data"
              :columns="stores.email.columns"
            >
            <template slot-scope="{ row }" slot="action">
              <Tooltip placement="top" content="删除" :delay="1000" :transfer="true">
              <Button
                type="error"
                size="small"
                shape="circle"
                icon="md-trash"
                @click="delete_email(row)"
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
            
            </template>
            </Table>
          </dz-table>
      </Card>
       
      </Form>
      
    </Card>

    <Drawer
      title="添加抓药师邮箱"
      v-model="stores.opened"
      width="500"
      :mask-closable="false"
      :mask="true"
      :styles="styles"
    >
      <!--添加修改弹出窗口-->
      <Form :model="emailModel.fields" ref="formNotice2" :rules="emailModel.rules" label-position="top">
       
      <Row :gutter="16" style="margin-top:20px;">
          <Col span="24">
            <FormItem label="邮箱号" prop="email" >
              <Input   v-model="emailModel.fields.email" placeholder="抓药师邮箱号" />
            </FormItem>
          </Col>
        </Row>
        
      </Form>
      <div class="demo-drawer-footer">
        <Button icon="md-checkmark-circle" type="primary" @click="handleSubmitEmail" >确 认</Button>
        <Button style="margin-left: 8px" icon="md-close" @click="stores.opened = false">取 消</Button>
      </div>
    </Drawer>
  </div>
</template>

<script>
import DzTable from "_c/tables/dz-table.vue";
import {
  getSystemInfo,
  updateSystem,
  emailList,
  emailUpdate,
  emailDelete,
  emailAdd,
} from "@/api/userAdmin";

export default {
  name: "system",
  components: {
    DzTable,
  },
  data() {
    return {
      userId:this.$store.state.adminUser.id,
      formModel: {
        fields: {
          id:1,
          sysEmail:"",
          sysAuthorization:"",
          graspCount:"",
          boidedMax:"",
          boidedMin:""
        },
        rules: {
          sysEmail: [{ type: "email",required: true, message: "请输入系统邮箱" }],
          sysAuthorization: [{ type: "string",required: true, message: "请输入邮箱邮箱授权码" }],
          graspCount: [{ type: "number", required: true, message: "请输入抓药师邮箱上限数量" }],
          boidedMax: [{ type: "number", required: true, message: "请输入煮药服数上限数量" }],
          boidedMin: [{ type: "number", required: true, message: "请输入煮药服数下限数量" }],
        },
      },
      emailModel: {
        fields: {
          id:0,
          email:"",
        },
        rules: {
          email: [{ type: "email",required: true, message: "请输入系统邮箱" }],
        },
      },
      stores: {
        mode: "create",
        opened:false,
        email:{
          query:{
            pageNum: 1,
            pageSize: 50,
            totalCount: 0,
          },
          columns: [
            { title: "序号", type: "index", width: 50 },
            { title: "邮箱号", key: "email" },
            {
              title: "操作",
              align: "center",
              className: "table-command-column",
              slot: "action",
            },
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
      maxadd:10

    };
  },
  mounted() {
    this.initdata();
  },
  methods: {
    //检测登录
    is_havalogin(){
      if(this.userId=='' || this.userId==undefined || this.userId==null){
          this.$Message.warning("请重新登录");
          this.$router.push({name:"login"});
      }
    },
    initdata() {
      this.is_havalogin();
      getSystemInfo().then(res=>{
        if(res.data.status==200){
          this.formModel.fields=res.data.data;
        }else{
          this.$Message.error(res.data.msg);
        }
      });
      var params={
        pageNum:this.stores.email.query.pageNum,
        pageSize:this.stores.email.query.pageSize
      };
      emailList(params).then(res=>{
        if(res.data.status==200){
          this.stores.email.data=res.data.data.list;
          this.stores.email.query.totalCount = res.data.data.total;
        }
      });
    },
    handleSubmitUser(){
      updateSystem(this.formModel.fields).then(res=>{
        if(res.data.status==200){
          this.initdata();
          this.$Message.success(res.data.msg);
        }else{
          this.$Message.error(res.data.msg);
        }
      });
    },
    delete_email(row){
      emailDelete({id:row.id}).then(res=>{
        if(res.data.status==200){
          this.initdata();
          this.$Message.error("删除成功");
        }else{
          this.$Message.error(res.data.msg);
        }
      });
    },
    handleAdd(){
      if(this.stores.email.query.totalCount>=this.formModel.fields.graspCount){
        this.$Message.warning("抓药师邮箱数量已达上限");
        return;
      }

      this.stores.mode="add";
      this.stores.opened=true;
    },
    handleEdit(row){
      this.emailModel.fields.email=row.email;
      this.emailModel.fields.id=row.id;
      this.stores.mode="edit";
      this.stores.opened=true;
    },
    handleSubmitEmail(){
      if(this.stores.mode=="add"){
          this.$refs["formNotice2"].validate(valid => {
          if (!valid) {
            this.$Message.error("请完善表单信息");
          } else {
            var params=new FormData();
            params.append("email",this.emailModel.fields.email);
            emailAdd(params).then(res=>{
              if(res.data.status==200){
                this.$Message.success("添加成功");
                this.emailModel.fields.email="";
                this.stores.opened=false;
                this.initdata();
              }else{
                this.$Message.error(res.data.msg);
              }
            });
          }
        });
      }else{
        this.$refs["formNotice2"].validate(valid => {
          if (!valid) {
            this.$Message.error("请完善表单信息");
          } else {
            var params=new FormData();
            params.append("email",this.emailModel.fields.email);
            params.append("id",this.emailModel.fields.id);
            emailUpdate(params).then(res=>{
              if(res.data.status==200){
                this.$Message.success("修改成功");
                this.emailModel.fields.email="";
                this.stores.opened=false;
                this.initdata();
              }else{
                this.$Message.error(res.data.msg);
              }
            });
          }
        });
      }
      

      
    }
  },
};
</script>

<style>

</style>
