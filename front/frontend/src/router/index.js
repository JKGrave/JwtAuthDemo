import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import TestVue from '@/components/TestVue'
import Authorization from '@/components/Authorization'

Vue.use(Router)

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
    }
  ]
})
