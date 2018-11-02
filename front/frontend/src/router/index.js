import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '../components/HelloWorld'
import TestVue from '../components/TestVue'
import Authorization from '../components/auth/Authorization'
import MyInfo from '../components/myinfo/MyInfo'
import store from '../store'

Vue.use(Router)

const requireAuth = () => (to, from, next) => {
  if (store.getters.isAuthenticated) return next()
  next('/login?returnPath=myInfo')
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/test',
      name: 'TestVue',
      component: TestVue
    },
    {
      path: '/login',
      name: 'Authorization',
      component: Authorization
    },
    {
      path: '/myInfo',
      name: 'MyInfo',
      component: MyInfo,
      beforeEnter: requireAuth()
    }
  ]
})
