#!/bin/bash -vxe
#!/bin/bash -e
list="alias boa buffer cgi cgi_header config escape get hash ip log mmap_cache pipe queue read request response 'select' signals sublog util"
list=alias
flags="-U HAVE_LIBDMALLOC"
srcPath=boa/src

for i in $list; do
  ./jcpp.sh $srcPath/$i.c $flags
done
for i in $list; do
  ./postProcess.sh $srcPath/$i.c $flags
done