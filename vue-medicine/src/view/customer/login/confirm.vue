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
          >Confirm Information</label
        >
        <div class="login-form" style="min-height:1000px;">
          <div class="sign-up-htm" style="margin-top:10px;">
            
            <div class="info">
              <label>User Name:</label>
              <span>{{info.firstName}}-{{info.lastName}}</span>
            </div>
            <div class="info">
              <label>City:</label>
              <span>{{info.city}}</span>
            </div>
            <div class="info">
              <label>Address:</label>
              <span>{{info.address}}</span>
            </div>
            <div class="info">
              <label>State:</label>
              <span>{{info.state}}</span>
            </div>
            <div class="info">
              <label>Email:</label>
              <span>{{info.email}}</span>
            </div>
            <div class="info">
              <label>Phone:</label>
              <span>{{info.phone}}</span>
            </div>

            <div class="hr"></div>
            <div class="info2">
              <p>
                I have Read and agreed to tecms and conditions.
                and private policy.
              </p>
            </div>
           

            <div class="group" style="margin-top:30px;">
              <input type="button" @click="changeinfo" class="button" value="Change" />
            </div>
            <div class="group" style="margin-top:30px;">
              <input type="button" @click="btn_confirm" class="button" value="Finish" />
            </div>
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
        id:""
      }
    };
  },
  methods: {
    //检测登录
    is_havalogin(){
      if(this.$store.state.userId=='' || this.$store.state.userId==undefined || this.$store.state.userId==null){
          this.$Message.warning("请重新登录");
          this.$router.push({name:"customerLogin"});
      }else{
          this.info.id=this.$store.state.userId;
      }
    },
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
      
      customerUpdateInfo(this.info).then(res=>{
        if(res.data.status==200){
          this.$Message.success("更新成功");
          this.$router.push(
            {name:"okay"}
          )
        }else{
          this.$Message.error(res.data.msg);
        }
      });
    },
    changeinfo(){
      this.$router.push(
        {name:"add_information"}
      )
    }
  },
  mounted(){
      if(this.$store.state.info!=undefined && this.$store.state.info!=""){
        this.info=this.$store.state.info;
        console.log(this.info);
      }
      this.is_havalogin();
  }
};
</script>
<style scoped>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
}
.info{
  color: #fff;
  overflow: hidden;
  font-size: 15px;
  padding:8px 0px;
}
.info label{
  left:left;
  width:30%;
}
.info span{
  float: right;
  width:70%;
  margin-left:10px;
}
.info2{
  color:#fff;
  /* font-size: 14px; */
}
@import "./login.css";
</style>


