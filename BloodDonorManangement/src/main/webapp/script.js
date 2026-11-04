document.addEventListener("DOMContentLoaded",()=>{
document.querySelectorAll(".counter").forEach(counter=>{
counter.innerText="0";
const update=()=>{
const target=+counter.dataset.target;
const current=+counter.innerText;
const inc=Math.ceil(target/100);
if(current<target){
counter.innerText=current+inc;
setTimeout(update,20);
}else{
counter.innerText=target;
}
};
update();
});
document.querySelectorAll('a[href^="#"]').forEach(link=>{
link.addEventListener("click",e=>{
const target=document.querySelector(link.getAttribute("href"));
if(target){
e.preventDefault();
target.scrollIntoView({behavior:"smooth"});
}
});
});
window.addEventListener("scroll",()=>{
const nav=document.querySelector(".navbar");
nav.style.boxShadow=window.scrollY>40?"0 5px 20px rgba(0,0,0,.18)":"none";
});
const img=document.querySelector(".hero-right img");
if(img){
let up=true;
setInterval(()=>{
img.style.transform=`translateY(${up?8:-8}px)`;
up=!up;
},1800);
}
const items=document.querySelectorAll(".info-card,.stat-card,.why-card,.about,.contact,.quote-section");
const observer=new IntersectionObserver(entries=>{
entries.forEach(entry=>{
if(entry.isIntersecting){
entry.target.style.opacity="1";
entry.target.style.transform="translateY(0)";
}
});
},{threshold:0.15});
items.forEach(item=>{
item.style.opacity="0";
item.style.transform="translateY(40px)";
item.style.transition="all .8s ease";
observer.observe(item);
});
});