const brandPage = figma.root.children.find((p) => p.id === '61084:30304');
await figma.setCurrentPageAsync(brandPage);
const existing = figma.currentPage.findOne((n) => n.name === 'Exploraciones logo Jun 2026');
if (existing) existing.remove();
const test = figma.currentPage.findOne((n) => n.id === '61110:3');
if (test) test.remove();
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
const buildA=function(d){const i=d?ON_DARK:INK;const r=figma.createFrame();r.layoutMode='HORIZONTAL';r.fills=[];r.appendChild(txt('My',32,'Fraunces','SemiBold',i));r.appendChild(txt('OwnTrip',32,'Fraunces','Regular',i,0.85));return r;};
const buildB=function(d){const i=d?ON_DARK:INK;const r=figma.createFrame();r.layoutMode='HORIZONTAL';r.itemSpacing=2;r.fills=[];r.appendChild(txt('My',32,'Inter','Regular',i,0.7));r.appendChild(txt('Own',34,'Fraunces','Italic',i));r.appendChild(txt('Trip',32,'Inter','Medium',i));r.appendChild(ribbon(20,OCRE));return r;};
const section=figma.createFrame();section.name='Exploraciones logo Jun 2026';section.x=0;section.y=760;section.layoutMode='VERTICAL';section.itemSpacing=24;section.paddingLeft=section.paddingRight=48;section.paddingTop=section.paddingBottom=48;section.fills=solid(SURFACE);
section.appendChild(txt('Exploraciones logo - memorabilidad',28,'Fraunces','SemiBold',INK));
section.appendChild(txt('A/B/C retomadas + D/E nuevas. Objetivo: menos generico.',14,'Inter','Regular',MUTED));
const ref=figma.createFrame();ref.layoutMode='VERTICAL';ref.itemSpacing=12;ref.fills=solid(PAPER);ref.strokes=solid(BORDER);ref.strokeWeight=1;ref.cornerRadius=12;ref.paddingLeft=ref.paddingRight=ref.paddingTop=ref.paddingBottom=20;ref.appendChild(txt('Referencia W4 actual',13,'Inter','Medium',INK));const rs=stage(false);const cur=figma.createFrame();cur.layoutMode='HORIZONTAL';cur.itemSpacing=4;cur.fills=[];cur.appendChild(txt('My',32,'Fraunces','SemiBold',INK));cur.appendChild(txt('Own',32,'Fraunces','Italic',INK));cur.appendChild(txt('Trip',32,'Fraunces','SemiBold',INK));cur.appendChild(ribbon(20,OCRE));rs.appendChild(cur);ref.appendChild(rs);section.appendChild(ref);
const grid=figma.createFrame();grid.name='Grid';grid.layoutMode='HORIZONTAL';grid.itemSpacing=20;grid.layoutWrap='WRAP';grid.counterAxisSpacing=20;grid.fills=[];grid.resize(1304,100);
grid.appendChild(makeCard('A','Wordmark Fraunces','Serif completo sin isotipo.',buildA));
grid.appendChild(makeCard('B','Hibrido tipografico','Inter + Own Fraunces italica.',buildB));
section.appendChild(grid);figma.currentPage.appendChild(section);
return { sectionId: section.id, gridId: grid.id };
