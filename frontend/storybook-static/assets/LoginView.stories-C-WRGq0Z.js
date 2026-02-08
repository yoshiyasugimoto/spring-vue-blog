import{d as B,l as M,c as s,b as a,r as o,u as C,e as N,o as _,f as T,h as A,g as H,a as O,m as U,j as D,n as j}from"./iframe-C3jG6tCW.js";import{u as z,w as F,b as P,c as R}from"./decorators-Blxr28FJ.js";import{_ as W}from"./_plugin-vue_export-helper-DlAUqK2U.js";import"./preload-helper-Dp1pzeXC.js";import"./data-BUIPDpdg.js";const $={class:"login-container"},E=B({__name:"LoginView",setup(r){const u=C(),i=N(),l=z(),t=D(""),n=j({username:"",password:""});async function h(){try{t.value="",await l.login(n.username,n.password);const w=i.query.redirect||"/admin";u.push(w)}catch{t.value="ユーザー名またはパスワードが正しくありません"}}return(w,e)=>{const S=o("a-input"),m=o("a-form-item"),V=o("a-input-password"),L=o("a-button"),k=o("a-form"),q=o("a-alert"),x=o("a-card");return _(),M("div",$,[s(x,{class:"login-card",bordered:!1},{default:a(()=>[e[4]||(e[4]=T("h2",{class:"login-title"},"ログイン",-1)),s(k,{model:n,onFinish:h,layout:"vertical"},{default:a(()=>[s(m,{label:"ユーザー名",name:"username",rules:[{required:!0,message:"ユーザー名を入力してください"}]},{default:a(()=>[s(S,{value:n.username,"onUpdate:value":e[0]||(e[0]=p=>n.username=p),size:"large"},null,8,["value"])]),_:1}),s(m,{label:"パスワード",name:"password",rules:[{required:!0,message:"パスワードを入力してください"}]},{default:a(()=>[s(V,{value:n.password,"onUpdate:value":e[1]||(e[1]=p=>n.password=p),size:"large"},null,8,["value"])]),_:1}),s(m,null,{default:a(()=>[s(L,{type:"primary","html-type":"submit",loading:A(l).loading,block:"",size:"large"},{default:a(()=>[...e[3]||(e[3]=[H(" ログイン ",-1)])]),_:1},8,["loading"])]),_:1})]),_:1},8,["model"]),t.value?(_(),O(q,{key:0,message:t.value,type:"error","show-icon":"",closable:"",onClose:e[2]||(e[2]=p=>t.value="")},null,8,["message"])):U("",!0)]),_:1})])}}}),G=W(E,[["__scopeId","data-v-eba1efd0"]]);E.__docgenInfo={exportName:"default",displayName:"LoginView",description:"",tags:{},sourceFiles:["/Users/sugimotoyoshiwataru/ai-driven-repository/spring-vue-blog/frontend/src/views/LoginView.vue"]};const Z={title:"Views/Auth/Login",component:G,tags:["autodocs"],decorators:[F(),P()]},c={},d={decorators:[r=>(R.login=async()=>{throw new Error("Invalid credentials")},r())],play:async({canvasElement:r})=>{const u=r.querySelector('#login_username input, input[type="text"]'),i=r.querySelector('#login_password input, input[type="password"]');if(u&&i){const l=Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype,"value").set;l.call(u,"testuser"),u.dispatchEvent(new Event("input",{bubbles:!0})),l.call(i,"wrongpassword"),i.dispatchEvent(new Event("input",{bubbles:!0}));const t=r.querySelector('button[type="submit"]');t&&t.click()}}};var g,v,y;c.parameters={...c.parameters,docs:{...(g=c.parameters)==null?void 0:g.docs,source:{originalSource:"{}",...(y=(v=c.parameters)==null?void 0:v.docs)==null?void 0:y.source}}};var b,f,I;d.parameters={...d.parameters,docs:{...(b=d.parameters)==null?void 0:b.docs,source:{originalSource:`{
  decorators: [story => {
    authApi.login = async () => {
      throw new Error('Invalid credentials');
    };
    return story();
  }],
  play: async ({
    canvasElement
  }) => {
    const usernameInput = canvasElement.querySelector('#login_username input, input[type="text"]') as HTMLInputElement;
    const passwordInput = canvasElement.querySelector('#login_password input, input[type="password"]') as HTMLInputElement;
    if (usernameInput && passwordInput) {
      // Simulate input
      const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value')!.set!;
      nativeInputValueSetter.call(usernameInput, 'testuser');
      usernameInput.dispatchEvent(new Event('input', {
        bubbles: true
      }));
      nativeInputValueSetter.call(passwordInput, 'wrongpassword');
      passwordInput.dispatchEvent(new Event('input', {
        bubbles: true
      }));

      // Click submit
      const submitBtn = canvasElement.querySelector('button[type="submit"]') as HTMLButtonElement;
      if (submitBtn) submitBtn.click();
    }
  }
}`,...(I=(f=d.parameters)==null?void 0:f.docs)==null?void 0:I.source}}};const ee=["Default","WithError"];export{c as Default,d as WithError,ee as __namedExportsOrder,Z as default};
