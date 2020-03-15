<template>
  <div>
    <div>
      
    </div>
    <div>
      <input type="text"  placeholder="用户名" v-model="userName">
      <input type="text"  placeholder="密码" v-model="password">
      <!--<button class="login_btn el-button el-button&#45;&#45;primary is-round" type="primary" round>登录</button>-->
      <el-button @click.native="login" type="primary" round :loading="isBtnLoading">登录</el-button>
      <div style="margin-top: 10px">
        <span style="color: #000099;" @click="login">账号登陆</span><span style="color: #A9A9AB">忘记密码？</span>
      </div>
    </div>
  </div>
</template>
 
 
 
<script>
//  import { userLogin } from '../../api/api';
 
  export default {
    data() {
      return {
        userName: '',
        password: '',
        isBtnLoading: false
      }
    },
    created () {
      if(JSON.parse( localStorage.getItem('user')) && JSON.parse( localStorage.getItem('user')).userName){
        this.userName = JSON.parse( localStorage.getItem('user')).userName;
        this.password = JSON.parse( localStorage.getItem('user')).password;
      }
    },
    computed: {
      btnText() {
        if (this.isBtnLoading) return '登录中...';
        return '登录';
      }
    },
    methods: {
      login() {
        if (!this.userName) {
          this.$message.error('请输入用户名');
          return;
        }
        if (!this.password) {
          this.$message.error('请输入密码');
          return;
        }
        window.localStorage.setItem('token', JSON.stringify(this.userName+this.password))
      }
    }
  }
</script>