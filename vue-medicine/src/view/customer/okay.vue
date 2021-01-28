<template>
  <div class="cotn_principal">
    <div class="cont_centrar">
      <div class="cont_login">
        <div class="cont_info_log_sign_up">
          <div class="col_md_login">
            <div class="cont_ba_opcitiy">
              <h2>{{user.firstName}}-{{user.lastName}}</h2>
              <p>Okay, You are all set</p>
              <button class="btn_login" @click="cambiar_login">Continue Shopping</button>
            </div>
          </div>
        </div>
        <div class="cont_forms">
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
      user:{}
    };
  },
  methods: {
    cambiar_login() {
      this.$router.push(
        {name:"home"}
      )
    },
  },
  created() {},
  mounted() {
    this.user=this.$store.state.info;
  },
};
</script>
<style scoped>
.cont_ba_opcitiy {
    position: relative;
    background-color: rgba(187, 168, 170, 0.79);
    width: 80%;
    border-radius: 3px;
    padding: 15px 0px;
    margin: 0 auto;
    margin-top: 60px;
}
.cont_login{
  transform: translateX(25%);
}
.cotn_principal{
  text-align: center;
}
    @import "./start.css";
</style>


