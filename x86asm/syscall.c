#include <stdio.h>
#include <stdlib.h>

extern void fuck();

static void *tg_pool[4096];
static int tg_pool_p = 0;

static char *tg_newstring(int len)
{
	return (char *)(tg_pool[tg_pool_p++] = malloc(len+1));
}

void *tg_newarray(unsigned long len, unsigned long v)
{
	unsigned long *t = (unsigned long *)malloc(len*sizeof(unsigned long));
	unsigned long i;
	tg_pool[tg_pool_p++] = t;
	for (i = 0; i < len; ++i)
		t[i] = v;
	return t;
}

void *tg_newrecord(unsigned long len)
{
	return tg_pool[tg_pool_p++] = (char *)malloc(len*sizeof(unsigned long));
}

void tg_print(char *s)
{
	printf("%s", s);
	fflush(stdout);
}

void tg_printi(int i)
{
	printf("%d", i);
	fflush(stdout);
}

char * tg_getchar()
{
	char c = getchar();
	char *p = tg_newstring(1);
	p[0] = c;
	p[1] = '\0';
	return p;
}

void tg_finalize()
{
	int i;
	for (i = 0; i < tg_pool_p; ++i)
		free(tg_pool[i]);
}

char *tg_chr(int v)
{
	char *s = tg_newstring(1);
	s[0] = v;
	s[1] = 0;
	return s;
}

int tg_ord(char *s)
{
	return s[0];
}

int tg_streq(char *s1, char *s2)
{
	return (!strcmp(s1,s2) ? 1 : 0);
}

int tg_strneq(char *s1, char *s2)
{
	return (!strcmp(s1,s2) ? 1 : 0);
}