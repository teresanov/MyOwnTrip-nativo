const brandPage = figma.root.children.find((p) => p.id === '61084:30304');
await figma.setCurrentPageAsync(brandPage);
const test = figma.currentPage.findOne((n) => n.id === '61110:3');
if (test) test.remove();
const grid = figma.currentPage.findOne((n) => n.name === 'Grid' && n.parent && n.parent.name === 'Exploraciones logo Jun 2026');
if (!grid) return { error: 'Grid not found' };
const fonts = [['Fraunces','Regular'],['Fraunces','Italic'],['Fraunces','SemiBold'],['Fraunces','Light'],['Fraunces','Bold'],['Inter','Regular'],['Inter','Medium']];
for (const f of fonts) await figma.loadFontAsync({ family: f[0], style: f[1] });
const INK = { r: 0.29, g: 0.345, b: 0.39 };
const PAPER = { r: 0.957, g: 0.941, b: 0.91 };
const OCRE = { r: 0.769, g: 0.514, b: 0.157 };
const ON_DARK = { r: 0.976, g: 0.937, b: 0.886 };
const DARK_BG = { r: 0.09, g: 0.075, b: 0.043 };
const BORDER = { r: 0.808, g: 0.776, b: 0.706 };
const MUTED = { r: 0.298, g: 0.275, b: 0.224 };
const TAG = { r: 0.243, g: 0.373, b: 0.565 };
const SURFACE = { r: 1, g: 0.973, b: 0.949 };
function solid(c,o){const f={type:'SOLID',color:{r:c.r,g:c.g,b:c.b}};if(o&&o<1)f.opacity=o;return[f];}
function txt(s,fs,fam,st,c,o){const t=figma.createText();t.characters=s;t.fontSize=fs;t.fontName={family:fam,style:st};t.fills=solid(c,o);return t;}
function ribbon(h,c){const w=h*0.51;const v=figma.createVector();const x1=w*0.29;const x2=w*0.71;const y1=h*0.74;v.vectorPaths=[{windingRule:'NONZERO',data:'M '+x1+' 0 L '+x2+' 0 L '+x2+' '+y1+' L '+(w*0.5)+' '+h+' L '+x1+' '+y1+' Z'}];v.fills=solid(c);v.resize(w,h);return v;}
function stage(dark){const s=figma.createFrame();s.layoutMode='HORIZONTAL';s.primaryAxisAlignItems='CENTER';s.counterAxisAlignItems='CENTER';s.paddingLeft=s.paddingRight=24;s.paddingTop=s.paddingBottom=28;s.minHeight=120;s.fills=solid(dark?DARK_BG:PAPER);return s;}
function makeCard(tag,title,desc,build){const card=figma.createFrame();card.name=tag+' - '+title;card.layoutMode='VERTICAL';card.fills=solid(PAPER);card.strokes=solid(BORDER);card.strokeWeight=1;card.cornerRadius=12;card.clipsContent=true;card.resize(340,100);const header=figma.createFrame();header.layoutMode='VERTICAL';header.itemSpacing=6;header.paddingLeft=header.paddingRight=20;header.paddingTop=header.paddingBottom=16;header.fills=solid(SURFACE);header.appendChild(txt(tag,11,'Inter','Medium',TAG));header.appendChild(txt(title,18,'Fraunces','SemiBold',INK));header.appendChild(txt(desc,13,'Inter','Regular',MUTED));card.appendChild(header);const light=stage(false);light.appendChild(build(false));card.appendChild(light);light.layoutSizingHorizontal='FILL';const dark=stage(true);dark.appendChild(build(true));card.appendChild(dark);dark.layoutSizingHorizontal='FILL';return card;}
const buildC=function(d){const i=d?ON_DARK:INK;const r=figma.createFrame();r.layoutMode='HORIZONTAL';r.itemSpacing=16;r.fills=[];const box=figma.createFrame();box.resize(56,59);box.fills=[];box.strokes=solid(i);box.strokeWeight=1;box.cornerRadius=4;box.layoutMode='HORIZONTAL';box.primaryAxisAlignItems='CENTER';box.counterAxisAlignItems='CENTER';box.appendChild(txt('M',30,'Fraunces','SemiBold',i));r.appendChild(box);r.appendChild(ribbon(24,OCRE));const col=figma.createFrame();col.layoutMode='VERTICAL';col.itemSpacing=2;col.fills=[];col.appendChild(txt('MyOwnTrip',22,'Fraunces','SemiBold',i));col.appendChild(txt('cuaderno de viaje',11,'Inter','Regular',i,0.65));r.appendChild(col);return r;};
const buildD=function(d){const i=d?ON_DARK:INK;const m=d?ON_DARK:INK;const r=figma.createFrame();r.layoutMode='HORIZONTAL';r.itemSpacing=12;r.fills=[];r.counterAxisAlignItems='CENTER';const mot=figma.createFrame();mot.layoutMode='HORIZONTAL';mot.itemSpacing=-2;mot.fills=[];mot.appendChild(txt('M',40,'Fraunces','Light',m,0.62));mot.appendChild(txt('O',40,'Fraunces','Bold',i));mot.appendChild(txt('T',40,'Fraunces','Light',m,0.62));mot.appendChild(ribbon(34,OCRE));r.appendChild(mot);r.appendChild(txt('My Own Trip',14,'Inter','Regular',i,0.75));return r;};
const buildE=function(d){const i=d?ON_DARK:INK;const col=figma.createFrame();col.layoutMode='VERTICAL';col.itemSpacing=8;col.counterAxisAlignItems='CENTER';col.fills=[];col.appendChild(ribbon(48,OCRE));const mot=figma.createFrame();mot.layoutMode='HORIZONTAL';mot.fills=[];mot.appendChild(txt('M',18,'Fraunces','Light',i,0.62));mot.appendChild(txt('O',18,'Fraunces','Bold',i));mot.appendChild(txt('T',18,'Fraunces','Light',i,0.62));col.appendChild(mot);return col;};
grid.appendChild(makeCard('C','Monograma M','Marco libreta + lockup.',buildC));
grid.appendChild(makeCard('D','MOT protagonista','Monograma grande + subtitulo.',buildD));
grid.appendChild(makeCard('E','Cinta isotipo','Marcapaginas como simbolo.',buildE));
return { ok: true, gridId: grid.id };
