import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ELementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

Vue.prototype.$http = axios
Vue.use(ELementUI)
/* eslint-disable no-new */
const V = new Vue({
  el: '#app',
  router,
  components: { App },
  render:(h)=>{
    return h(App)
  }
})

router.beforeEach((to, from, next) => {
  debugger
  if (to.meta.title) {
    document.title = to.meta.title
  }
  const type = to.meta.type
  // 判断该路由是否需要登录权限
  if (type === 'login') {
    if (window.localStorage.getItem('token')) {
      next()
    } else {
      next('/login')
      return
    }
  } else {
    next()  // 确保一定要有next()被调用
  }
})
export default V
router.start(App, '#app')
