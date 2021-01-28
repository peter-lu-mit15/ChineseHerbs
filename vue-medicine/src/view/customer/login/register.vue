<template>
  <div class="jq22-container" style="padding-top: 50px">
    <div class="login-wrap">
      <div class="login-html">
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
          >Sign Up</label
        >
        <div class="login-form">
          <div class="sign-up-htm" style="margin-top:10px;">
            <div class="group">
              <label for="user" class="label">Phone Number</label>
              <input id="user" v-model="user.username" type="number" class="input" />
            </div>
            <div class="group">
              <label for="pass" class="label">Password</label>
              <input
                id="pass"
                type="password"
                class="input"
                data-type="password"
                v-model="user.password"
              />
            </div>
            <div class="group">
              <label for="pass" class="label">Repeat Password</label>
              <input
                id="pass"
                type="password"
                class="input"
                data-type="password"
                v-model="user.repassword"
              />
            </div>
            
            <div class="group">
              <input type="button" @click="btn_register" class="button" value="Sign Up" />
            </div>
            <div class="hr"></div>
            <div class="foot-lnk">
              <a href="javascript:void(0)" @click="to_login">Sign In</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {customerRegister} from "@/api/customer"
export default {
  name: "login",
  data() {
    return {
      user:{
        username:"",
        password:"",
        repassword:"",
      }
    };
  },
  methods: {
    btn_register(){
      if(this.user.username=="" || this.user.password=="" || this.user.repassword==""){
        this.$Message.warning("请先填写完整信息");
        return;
      }
      if(this.user.password.length<6){
        this.$Message.warning("密码长度不能少于6位");
        return;
      }
      if(this.user.password!=this.user.repassword){
        this.$Message.warning("密码不一致，请检查密码");
        return;
      }
      var params=new FormData();
      params.append("username",this.user.username);
      params.append("password",this.user.password);
      customerRegister(params).then(res=>{
        if(res.data.status==200){
          this.$Message.success("注册成功");
          this.$store.state.userId=res.data.data.id;
          this.$store.state.userinfo=res.data.data;
          this.$router.push(
            {name:"add_information"}
          )
        }else{
          this.$Message.error(res.data.msg);
        }
      });
    },
    to_login(){
      this.$router.push({name:"customerLogin"});
    }
  },
  created() {},
  mounted() {},
};
</script>
<style scoped>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
}
@import "./login.css";
</style>


