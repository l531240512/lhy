// export function htttpRequest (url, params, requestType, cb) {
//     let dataStr = ''
//     for (let key in params) {
//       dataStr += key + '=' + params[key]
//     }
//     dataStr = dataStr.substr(0, dataStr.length - 1)
  
//     AjaxPlugin.$http.post(url, dataStr)
//     .then((response) => {
//       if (cb) cb(response.data)
//     })
//   }