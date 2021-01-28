<template>
  <div class="jq22-container" style="padding-top: 50px">
    <div class="login-wrap">
      <div class="login-html">
        <input
          id="tab-1"
          type="radio"
          name="tab"
          class="sign-in"
          checked
        />
        <label for="tab-1" class="tab" style="margin:0 auto;position: relative;left:50%;transform:translateX(-50%);">Sign In</label>
        <input id="tab-2" type="radio" name="tab" class="sign-up" style="display:none;" /><label
          for="tab-2"
          class="tab"
          style="display:none;"
          >Sign Up</label
        >
        <div class="login-form">
          <div class="sign-in-htm" style="margin-top:10px;">
            <div class="group">
              <label for="user" class="label">Phone Number</label>
              <input id="user" v-model="loginForm.username" type="number" class="input" />
            </div>
            <div class="group">
              <label for="pass" class="label">Password</label>
              <input
                id="pass"
                type="password"
                class="input"
                data-type="password"
                v-model="loginForm.password" 
              />
            </div>
            <!-- <div class="group">
              <input id="check" type="checkbox" class="check" checked />
              <label for="check"
                ><span class="icon"></span> Keep me Signed in</label
              >
            </div> -->
            <div class="group">
              <input type="button" @click="btn_login" class="button" value="Sign In" />
            </div>
            <div class="hr"></div>
            <div class="foot-lnk">
              <a href="javascript:void(0)" @click="to_register">Sign Up</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {customerLogin} from "@/api/customer"
export default {
  name: "login",
  data() {
    return {
      loginForm:{
        username:"",
        password:""
      }
    };
  },
  methods: {
    btn_login(){
      if(this.loginForm.username=="" || this.loginForm.password==""){
        this.$Message.warning("请填写账号密码");
        return;
      }
      var params=new FormData();
      params.append("username",this.loginForm.username);
      params.append("password",this.loginForm.password);
      customerLogin(params).then(res=>{
        if(res.data.status==200){
          this.$Message.success("登录成功");
          console.log(res.data.data);
          this.$store.state.userId=res.data.data.id;
          this.$store.state.userinfo=res.data.data;
          this.$router.push(
            {name:"home"}
          )
        }else{
          this.$Message.error(res.data.msg);
        }
      });
    },
    to_register(){
      this.$router.push({name:"customer_register"});
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


