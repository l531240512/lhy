import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: 'login',
    },
    {
      path: '/demo',
      component: require('../demo/index.vue').default,
      name: 'demo',
      meta: {
        title: 'demo',
        type: 'login',
        keepAlive: false,
        requireAuth: true
      }
    },
    {
      path: '/login',
      component: require('../login/index.vue').default,
      name: 'login',
      meta: {
        title: 'login',
        type: '',
        keepAlive: false
      }
    }
  ]
})
