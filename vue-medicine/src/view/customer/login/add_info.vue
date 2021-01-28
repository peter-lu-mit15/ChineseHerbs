<template>
  <div class="jq22-container" >
    <div style="min-height:1000px;" class="login-wrap">
      <div class="login-html" style="padding-top: 40px;">
        <input
          id="tab-1"
          type="radio"
          name="tab"
          class="sign-in"
        /><label for="tab-1" class="tab" style="display:none;">Sign In</label>
        <input id="tab-2" type="radio" name="tab" class="sign-up" checked /><label
          for="tab-2"
          class="tab"
          style="margin:0 auto;position: relative;left:50%;transform:translateX(-50%);"
          >Information</label
        >
        <div class="login-form" style="min-height:1000px;">
          <div class="sign-up-htm" style="margin-top:10px;">
            <!-- <Form :model="emailModel.fields" ref="formNotice" :rules="emailModel.rules" label-position="top">
              <Row :gutter="16" style="margin-top:20px;">
                  <Col span="24">
                    <FormItem label="邮箱号" prop="email" >
                      <Input   v-model="emailModel.fields.email" placeholder="抓药师邮箱号" />
                    </FormItem>
                  </Col>
              </Row>
            </Form> -->
            <div class="group">
              <label  class="label">First Name</label>
              <input v-model="info.firstName" type="text" class="input" />
            </div>
            <div class="group">
              <label  class="label">Last Name</label>
              <input v-model="info.lastName" type="text" class="input" />
            </div>
            <div class="group">
              <label  class="label">Street Address </label>
              <input v-model="info.address" type="text" class="input" />
            </div>
            <div class="group">
              <label  class="label">City </label>
              <input v-model="info.city" type="text" class="input" />
            </div>
            <div class="group">
              <label  class="label">State </label>
              <input v-model="info.state" type="text" class="input" />
            </div>
            <div class="group">
              <label  class="label">Email </label>
              <input v-model="info.email" type="email" class="input" />
            </div>
            <div class="group">
              <label  class="label">Phone </label>
              <input v-model="info.phone" type="number" class="input" />
            </div>

            <div class="group">
              <input type="button" @click="btn_confirm" class="button" value="Continue" />
            </div>
            <div class="hr"></div>
            <!-- <div class="foot-lnk">
              <label for="tab-1">Already Member?</label>
            </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {customerUpdateInfo} from "@/api/customer"

export default {
  name: "login",
  data() {
    return {
      info:{
        firstName:"",
        lastName:"",
        city:"",
        address:"",
        state:"",
        email:"",
        phone:"",
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
    };
  },
  methods: {
    btn_confirm(){
      if(this.info.firstName=="" || this.info.lastName=="" ){
        this.$Message.warning("用户名不能为空");
        return;
      }
      if(this.info.email=="" ){
        this.$Message.warning("email地址不能为空");
        return;
      }
      if(this.info.phone=="" ){
        this.$Message.warning("用户手机号不能为空");
        return;
      }
      this.$store.state.info=this.info;
          this.$router.push(
            {name:"confirmpage"}
          )
      // customerUpdateInfo(this.info).then(res=>{
      //   if(res.data.status==200){
      //     this.$Message.success("更新成功");
      //     this.$router.push(
      //       {name:"confirmpage"}
      //     )
      //   }else{
      //     this.$Message.error(res.data.msg);
      //   }
      // });
    },
  },
  mounted () {
      if(this.$store.state.info!=undefined && this.$store.state.info!=""){
        this.info=this.$store.state.info;
        console.log(this.info);
      }
    }
};
</script>
<style scoped>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
}
@import "./login.css";
</style>


