function fn(){
var env = karate.env;
Karate.log('karate.env system properties was:', env)// get system property 'karate.env'
if(!env){
env = 'dev';
}

var config = {
 env: env,
 baseUrl = 'https://e2e.example.com/api';
}

if(env == 'dev'){
config.SOMEvAR = 'devSomeVar'

} else if (env == 'e2e'){
    config.someVar = 'e2eSomeVar'
}

return config;
}
