<template>
  <div class="cotn_principal">
    <div class="cont_centrar">
      <div class="cont_login">
        <div class="cont_info_log_sign_up">
          <div class="col_md_login">
            <div class="cont_ba_opcitiy">
              <h2>LOGIN</h2>
              <p>Welcome to login.</p>
              <button class="btn_login" @click="cambiar_login">LOGIN</button>
            </div>
          </div>
          <div class="col_md_sign_up">
            <div class="cont_ba_opcitiy">
              <h2>SIGN UP</h2>
              <p>Welcome to sign up.</p>
              <button class="btn_sign_up" @click="cambiar_sign_up()">
                SIGN UP
              </button>
            </div>
          </div>
        </div>
        <div class="cont_back_info">
        </div>
        <div class="cont_forms">
          <div class="cont_form_login">
          </div>
          <div class="cont_form_sign_up">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>

export default {
  name: "login",
  data() {
    return {
      uploadLoading: false,
      progressPercent: 0,
      showProgress: false,
      showRemoveFile: false,
    };
  },
  methods: {
    cambiar_login() {
      this.$router.push(
            {name:"customerLogin"}
          )
    },
    cambiar_sign_up(at) {
      this.$router.push(
            {name:"customer_register"}
          )
    },
  },
};
</script>
<style scoped>
.cotn_principal{
  text-align: center;
}
.cont_ba_opcitiy {
    position: relative;
    background-color: rgba(187, 168, 170, 0.79);
    width: 80%;
    border-radius: 3px;
    padding: 15px 0px;
    margin: 0 auto;
    margin-top: 60px;
}
    @import "./start.css";
</style>


